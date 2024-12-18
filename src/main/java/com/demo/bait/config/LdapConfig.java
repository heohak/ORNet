package com.demo.bait.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;


@Configuration
public class LdapConfig {

    @Bean
    public LdapTemplate ldapTemplate(DefaultSpringSecurityContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }
}
