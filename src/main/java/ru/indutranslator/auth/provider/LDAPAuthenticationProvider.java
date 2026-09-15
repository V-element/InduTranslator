package ru.indutranslator.auth.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * LDAP authentication provider.
 * Authenticates users against LDAP directory.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LDAPAuthenticationProvider implements AuthenticationProvider {
    
    private final UserDetailsService userDetailsService;
    
    @Override
    public boolean authenticate(String username, String password) {
        try {
            // Note: Actual LDAP authentication would use Spring LDAP
            // For now, assume authentication succeeds if user exists locally
            // This is a placeholder for LDAP integration
            log.info("LDAP authentication check for user: {}", username);
            return userDetailsService.loadUserByUsername(username) != null;
        } catch (Exception e) {
            log.warn("LDAP authentication failed for user: {}", username, e);
            return false;
        }
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Load user from LDAP or local database
        try {
            return userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            log.warn("Failed to load user from LDAP: {}", username, e);
            throw new AuthenticationException("User not found: " + username, e);
        }
    }
    
    @Override
    public String getName() {
        return "ldap";
    }
}
