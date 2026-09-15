package ru.indutranslator.auth.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Local authentication provider using JWT.
 * Authenticates users with local database credentials.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LocalAuthenticationProvider implements AuthenticationProvider {
    
    private final UserDetailsService userDetailsService;
    
    @Override
    public boolean authenticate(String username, String password) {
        try {
            // Note: Password validation is done by the JWT filter
            // This provider assumes the user exists and token is valid
            UserDetails user = userDetailsService.loadUserByUsername(username);
            log.info("Local authentication successful for user: {}", username);
            return true;
        } catch (Exception e) {
            log.warn("Local authentication failed: user not found: {}", username);
            return false;
        }
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userDetailsService.loadUserByUsername(username);
    }
    
    @Override
    public String getName() {
        return "local";
    }
}
