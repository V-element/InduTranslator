package ru.indutranslator.auth.provider;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Authentication provider interface.
 * Implements Strategy pattern for different authentication methods.
 */
public interface AuthenticationProvider {
    
    /**
     * Authenticate user with credentials.
     * @param username username
     * @param password password
     * @return true if authentication successful
     */
    boolean authenticate(String username, String password);
    
    /**
     * Load user details by username.
     * @param username username
     * @return UserDetails
     */
    UserDetails loadUserByUsername(String username);
    
    /**
     * Get provider name.
     * @return provider name (local, ldap, keycloak)
     */
    String getName();
}
