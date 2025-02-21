package com.demo.bait.controller.AuthController;

import com.demo.bait.Security.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        log.debug("Login attempt received for username: {}", username);

        // Bypass for admin test user
        if ("admin".equals(username) && "admin".equals(password)) {
            log.debug("Bypassing LDAP authentication for admin user: {}", username);
            List<String> roles = List.of("ROLE_CRMADMINS");
            // List<String> roles = List.of("ROLE_ADMINISTRATORS");
            String token = jwtUtils.generateToken(username, roles);
            log.debug("JWT token generated for admin user: {}", username);
            return ResponseEntity.ok(Map.of("token", token));
        } 
        // Bypass for regular test user
        else if ("user".equals(username) && "user".equals(password)) {
            log.debug("Bypassing LDAP authentication for test user: {}", username);
            List<String> roles = List.of("ROLE_CRMUSERS");
            // List<String> roles = List.of("ROLE_USERS");
            String token = jwtUtils.generateToken(username, roles);
            log.debug("JWT token generated for test user: {}", username);
            return ResponseEntity.ok(Map.of("token", token));
        }

        try {
            log.debug("Attempting LDAP authentication for username: {}", username);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            log.debug("LDAP authentication successful for username: {}", username);

            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            log.debug("Retrieved roles for username {}: {}", username, roles);

            String token = jwtUtils.generateToken(username, roles);
            log.debug("JWT token generated for LDAP authenticated user: {}", username);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException ex) {
            log.error("LDAP authentication failed for username: {}", username, ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("Logout request received with invalid or missing Authorization header");
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or missing Authorization header"));
        }
        log.debug("Logout request processed with valid Authorization header");
        return ResponseEntity.ok(Map.of("message", "Logged out successfully!"));
    }
}
