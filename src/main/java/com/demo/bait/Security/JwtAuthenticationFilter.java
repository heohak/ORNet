package com.demo.bait.Security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/login")) {
            chain.doFilter(request, response); // Skip JWT validation for login
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        try {
            if (jwtUtils.validateToken(token)) {
                String username = jwtUtils.getUsernameFromToken(token);
                List<String> roles = jwtUtils.getRolesFromToken(token); // Extract roles from token

                // Convert roles to SimpleGrantedAuthority
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, authorities
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Refresh the token if close to expiry (e.g., within 10 minutes)
                Date expiration = jwtUtils.getExpirationDateFromToken(token);
//                System.out.println("################################");
//                System.out.println(expiration);
//                System.out.println("################################");
                long timeToExpire = expiration.getTime() - System.currentTimeMillis();
                if (timeToExpire < 4 * 60 * 60 * 1000) { // Less than 10 minutes = 10 * 60 * 1000
                    System.out.println("refresh token");
                    log.info("Refresh token");
                    String newToken = jwtUtils.refreshToken(token);
//                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//                    System.out.println(jwtUtils.getExpirationDateFromToken(newToken));
//                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                    response.setHeader("Authorization", "Bearer " + newToken);
                }
            }
        } catch (Exception e) {
            // Log the exception and proceed
            System.err.println("JWT validation failed: " + e.getMessage());
        }

        chain.doFilter(request, response);
    }
}
