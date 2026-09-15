package ru.indutranslator.ai.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Circuit breaker for AI provider calls.
 * Prevents cascading failures when provider is down.
 */
@Slf4j
@Component
public class CircuitBreaker {
    
    private static final int FAILURE_THRESHOLD = 3;
    private static final long RECOVERY_TIMEOUT_MS = 30000;
    
    private final Map<String, CircuitState> states = new HashMap<>();
    
    /**
     * Check if call is allowed for provider.
     * @param providerName provider name
     * @return true if call is allowed
     */
    public boolean allowCall(String providerName) {
        CircuitState state = states.computeIfAbsent(providerName, k -> new CircuitState());
        
        synchronized (state) {
            if (state.getState() == State.CLOSED) {
                return true;
            }
            
            if (state.getState() == State.OPEN) {
                if (System.currentTimeMillis() - state.getLastFailureTime() > RECOVERY_TIMEOUT_MS) {
                    // Half-open state - allow one call
                    state.setState(State.HALF_OPEN);
                    log.info("Circuit breaker for {} entering HALF_OPEN state", providerName);
                    return true;
                }
                return false;
            }
            
            // HALF_OPEN state
            return true;
        }
    }
    
    /**
     * Record successful call.
     * @param providerName provider name
     */
    public void recordSuccess(String providerName) {
        CircuitState state = states.computeIfAbsent(providerName, k -> new CircuitState());
        
        synchronized (state) {
            if (state.getState() == State.HALF_OPEN) {
                state.setState(State.CLOSED);
                state.setFailureCount(0);
                log.info("Circuit breaker for {} CLOSED (success)", providerName);
            }
            state.setLastSuccessTime(System.currentTimeMillis());
        }
    }
    
    /**
     * Record failed call.
     * @param providerName provider name
     */
    public void recordFailure(String providerName) {
        CircuitState state = states.computeIfAbsent(providerName, k -> new CircuitState());
        
        synchronized (state) {
            state.setFailureCount(state.getFailureCount() + 1);
            state.setLastFailureTime(System.currentTimeMillis());
            
            if (state.getFailureCount() >= FAILURE_THRESHOLD) {
                state.setState(State.OPEN);
                log.warn("Circuit breaker for {} OPEN (failure threshold reached)", providerName);
            }
        }
    }
    
    /**
     * Reset circuit state for provider.
     * @param providerName provider name
     */
    public void reset(String providerName) {
        CircuitState state = states.computeIfAbsent(providerName, k -> new CircuitState());
        
        synchronized (state) {
            state.setState(State.CLOSED);
            state.setFailureCount(0);
            log.info("Circuit breaker for {} RESET", providerName);
        }
    }
    
    /**
     * Get circuit state for provider.
     * @param providerName provider name
     * @return circuit state
     */
    public CircuitBreaker.State getState(String providerName) {
        CircuitState state = states.computeIfAbsent(providerName, k -> new CircuitState());
        return state.getState();
    }
    
    /**
     * Circuit state enum.
     */
    public enum State {
        CLOSED,    // Normal operation
        OPEN,      // Circuit tripped, no calls allowed
        HALF_OPEN  // Testing if service recovered
    }
    
    /**
     * Circuit state class.
     */
    private static class CircuitState {
        private State state = State.CLOSED;
        private int failureCount = 0;
        private long lastFailureTime = 0;
        private long lastSuccessTime = 0;
        
        public State getState() {
            return state;
        }
        
        public void setState(State state) {
            this.state = state;
        }
        
        public int getFailureCount() {
            return failureCount;
        }
        
        public void setFailureCount(int failureCount) {
            this.failureCount = failureCount;
        }
        
        public long getLastFailureTime() {
            return lastFailureTime;
        }
        
        public void setLastFailureTime(long lastFailureTime) {
            this.lastFailureTime = lastFailureTime;
        }
        
        public long getLastSuccessTime() {
            return lastSuccessTime;
        }
        
        public void setLastSuccessTime(long lastSuccessTime) {
            this.lastSuccessTime = lastSuccessTime;
        }
    }
}
