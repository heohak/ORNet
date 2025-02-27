package com.demo.bait.config;

import com.demo.bait.Security.CustomAuthenticationEntryPoint;
import com.demo.bait.Security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${ldap.bind.user}")
    private String ldapBindUser;

    @Value("${ldap.bind.password}")
    private String ldapBindPassword;
    
    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        log.info("Initializing LDAP context source with URL: ldap://bait-dc.bait.local:389 and base DN: DC=bait,DC=local");
        
        // Create the context source
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(
                List.of("ldap://bait-dc.bait.local:389"),
                "DC=bait,DC=local");

        // Set bind DN and password from the injected values
        contextSource.setUserDn(ldapBindUser);
        contextSource.setPassword(ldapBindPassword);

        return contextSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new LdapShaPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {
        log.info("Configuring security filter chain");
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                // Adjust role names as required; Spring Security prefixes roles with "ROLE_"
                .requestMatchers("/api/admin/**").hasRole("CRMADMINS")
                .requestMatchers("/api/**").hasAnyRole("CRMUSERS", "CRMADMINS")
                .anyRequest().authenticated())
            .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(DefaultSpringSecurityContextSource contextSource) throws Exception {
        log.info("Configuring LDAP authentication manager");
        AuthenticationManagerBuilder authBuilder = new AuthenticationManagerBuilder(new ObjectPostProcessor<>() {
            @Override
            public <O> O postProcess(O object) {
                return object;
            }
        });

        // Define the LDAP authorities populator to search for groups under "CN=Users,DC=bait,DC=local"
        DefaultLdapAuthoritiesPopulator authoritiesPopulator =
                new DefaultLdapAuthoritiesPopulator(contextSource, "CN=Users");
        // authoritiesPopulator.setGroupSearchFilter("(member={0})");
        String groupFilter = "(&(member={0})(|(cn=CRMusers)(cn=CRMadmins)))";
        authoritiesPopulator.setGroupSearchFilter(groupFilter);
        authoritiesPopulator.setGroupRoleAttribute("cn");
        authoritiesPopulator.setConvertToUpperCase(true);

        // Use BindAuthenticator to authenticate via LDAP bind with user credentials
        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
        // NOTE: Ensure that the login username does not include a domain prefix
        // bindAuthenticator.setUserDnPatterns(new String[]{
        //     "CN={0},OU=SBSUsers,OU=Users,OU=MyBusiness,DC=bait,DC=local"
        // });
        bindAuthenticator.setUserSearch(
            new FilterBasedLdapUserSearch(
                "OU=SBSUsers,OU=Users,OU=MyBusiness",
                "(userPrincipalName={0})",
                contextSource
            )
        );

        // Create the LDAP authentication provider using the bind authenticator and authorities populator
        LdapAuthenticationProvider ldapAuthenticationProvider =
                new LdapAuthenticationProvider(bindAuthenticator, authoritiesPopulator);
        authBuilder.authenticationProvider(ldapAuthenticationProvider);

        log.info("LDAP authentication provider configured successfully");
        return authBuilder.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Configuring CORS settings");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://192.168.1.49:3000", "http://ornetserver:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
