package com.demo.bait.config;

import com.demo.bait.Security.CustomAuthenticationEntryPoint;
import com.demo.bait.Security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.PasswordComparisonAuthenticator;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        // Configure the LDAP server URL and base DN
        return new DefaultSpringSecurityContextSource(
                List.of("ldap://localhost:10389"), "dc=example,dc=com");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new LdapShaPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMINISTRATORS") // Administrators only
                        .requestMatchers("/api/user/**").hasRole("USERS")          // Users only
                        .anyRequest().authenticated()
//                        .anyRequest().permitAll()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(DefaultSpringSecurityContextSource contextSource) throws Exception {
        AuthenticationManagerBuilder authBuilder = new AuthenticationManagerBuilder(new ObjectPostProcessor<>() {
            @Override
            public <O> O postProcess(O object) {
                return object;
            }
        });

        // Define an LdapAuthoritiesPopulator
        DefaultLdapAuthoritiesPopulator authoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource, "ou=groups");
        authoritiesPopulator.setGroupSearchFilter("(member={0})"); // Matches groups by membership
        authoritiesPopulator.setGroupRoleAttribute("cn"); // Use the group's 'cn' as the role name
        authoritiesPopulator.setConvertToUpperCase(true); // Roles will be in uppercase (e.g., ROLE_ADMINISTRATORS)

        // Configure the PasswordComparisonAuthenticator
        PasswordComparisonAuthenticator authenticator = new PasswordComparisonAuthenticator(contextSource);
        authenticator.setUserDnPatterns(new String[]{"uid={0},ou=users"}); // User DN pattern
        authenticator.setPasswordEncoder(passwordEncoder());
        authenticator.setPasswordAttributeName("userPassword");

        // Create the LdapAuthenticationProvider
        LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(authenticator, authoritiesPopulator);

        // Add the custom LDAP AuthenticationProvider
        authBuilder.authenticationProvider(ldapAuthenticationProvider);

        return authBuilder.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

