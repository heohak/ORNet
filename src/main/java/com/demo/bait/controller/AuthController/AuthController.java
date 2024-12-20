package com.demo.bait.controller.AuthController;

import com.demo.bait.Security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

        if ("admin".equals(username) && "admin".equals(password)) {
            // Assign CRMADMIN role
            List<String> roles = List.of("ROLE_CRMADMIN");
            // List<String> roles = List.of("ROLE_ADMINISTRATORS");

            // Generate a JWT token for the test admin user
            String token = jwtUtils.generateToken(username, roles);

            return ResponseEntity.ok(Map.of("token", token));
        } else if ("user".equals(username) && "user".equals(password)) {
            // Assign CRMUSER role
            List<String> roles = List.of("ROLE_CRMUSER");
            // List<String> roles = List.of("ROLE_USERS");

            // Generate a JWT token for the test user
            String token = jwtUtils.generateToken(username, roles);

            return ResponseEntity.ok(Map.of("token", token));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            String token = jwtUtils.generateToken(username, roles);

            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or missing Authorization header"));
        }
        return ResponseEntity.ok(Map.of("message", "Logged out successfully!"));
    }
}
