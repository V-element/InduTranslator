package ru.indutranslator.ai.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Retry strategy for AI provider calls.
 * Implements exponential backoff retry logic.
 */
@Slf4j
@Component
public class RetryStrategy {
    
    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_DELAY_MS = 1000;
    private static final double MULTIPLIER = 2.0;
    
    /**
     * Execute operation with retry.
     * @param operation operation to execute
     * @param <T> return type
     * @return result of operation
     * @throws RuntimeException if all retries fail
     */
    public <T> T executeWithRetry(Operation<T> operation) {
        int attempt = 0;
        long delay = INITIAL_DELAY_MS;
        
        while (attempt < MAX_RETRIES) {
            try {
                log.info("Attempt {}/{}: executing operation", attempt + 1, MAX_RETRIES);
                return operation.execute();
            } catch (Exception e) {
                attempt++;
                log.warn("Attempt {}/{} failed: {}", attempt, MAX_RETRIES, e.getMessage());
                
                if (attempt >= MAX_RETRIES) {
                    log.error("All {} attempts failed", MAX_RETRIES);
                    throw new RuntimeException("Operation failed after " + MAX_RETRIES + " attempts", e);
                }
                
                // Wait before retry
                try {
                    Thread.sleep(delay);
                    delay = (long) (delay * MULTIPLIER);
                    log.info("Retrying in {} ms", delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }
        
        throw new RuntimeException("Operation failed after " + MAX_RETRIES + " attempts");
    }
    
    /**
     * Operation interface for retryable operations.
     * @param <T> return type
     */
    @FunctionalInterface
    public interface Operation<T> {
        T execute() throws Exception;
    }
}
