package ru.indutranslator.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.indutranslator.domain.dto.AIResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {
        String errorMessage = ex.getMessage();
        if (errorMessage == null) errorMessage = "unavailable";

        // Check if this is an AI-related error
        boolean isAIError = errorMessage.contains("No active AI configuration") || 
            errorMessage.contains("Bearer") || 
            errorMessage.contains("token") || 
            errorMessage.contains("key") ||
            errorMessage.contains("API key") ||
            errorMessage.contains("configuration") ||
            request.getRequestURI().contains("/api/ai");

        if (isAIError && request.getRequestURI().contains("/api/ai")) {
            log.warn("AI error: {}", errorMessage);
            String fallback;
            if (errorMessage.contains("No active AI configuration") || 
                errorMessage.contains("Bearer") || 
                errorMessage.contains("token") || 
                errorMessage.contains("key") ||
                errorMessage.contains("configuration")) {
                fallback = "Не могу ответить: требуется API-ключ. Настройте GigaChat в настройках (⚙️). " +
                    "Функция AI-чата работает через внешний API Sber GigaChat.";
            } else {
                fallback = "Не могу ответить: " + errorMessage + ". Попробуйте позже.";
            }
            
            AIResponseDto errorResponse = AIResponseDto.builder()
                .id(UUID.randomUUID().toString())
                .object("chat.completion")
                .created(System.currentTimeMillis() / 1000)
                .model("gigachat-local")
                .choices(List.of(AIResponseDto.Choice.builder()
                    .text(fallback)
                    .finishReason("stop")
                    .build()))
                .build();
            return ResponseEntity.status(200).body(errorResponse);
        }

        // Default error response for other runtime exceptions
        log.error("Exception during request to {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());
        errorResponse.setPath(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request) {
        log.error("Unexpected exception during request to {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage("An unexpected error occurred");
        errorResponse.setTimestamp(System.currentTimeMillis());
        errorResponse.setPath(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    @Data
    public static class ErrorResponse {
        private String error;
        private String message;
        private Long timestamp;
        private String path;
    }
}
