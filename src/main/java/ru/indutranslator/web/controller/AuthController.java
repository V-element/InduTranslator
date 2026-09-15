package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.UserDto;
import ru.indutranslator.service.UserService;
import ru.indutranslator.utils.JwtTokenProvider;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class LoginResponse {
        private String token;
        private UserDto user;
    }

    @Data
    public static class ErrorResponse {
        private String error;
        private String message;
    }

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto, 1L);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            UserDto userDto = userService.getUserByUsername(loginRequest.username);
            
            // Generate real JWT token
            String token = jwtTokenProvider.generateToken(userDto.getUsername());
            
            LoginResponse response = new LoginResponse();
            response.token = token;
            response.user = userDto;
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError("Unauthorized");
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(@RequestParam String username) {
        UserDto userDto = userService.getUserByUsername(username);
        return ResponseEntity.ok(userDto);
    }
}

