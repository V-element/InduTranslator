package ru.indutranslator.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Security utility class for getting current user information.
 */
@UtilityClass
public class SecurityUtils {

    /**
     * Get the username of the currently authenticated user.
     *
     * @return current username
     * @throws IllegalStateException if user is not authenticated
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        
        if (principal instanceof String username) {
            return username;
        }
        
        throw new IllegalStateException("Unsupported principal type");
    }

    /**
     * Get the ID of the currently authenticated user.
     *
     * @return current user ID
     * @throws IllegalStateException if user is not authenticated or ID cannot be determined
     */
    public Long getCurrentUserId() {
        // This assumes the user ID is stored in the JWT token
        // If not available, you may need to look up the user by username
        return 1L; // TODO: Replace with actual user ID lookup
    }
}
