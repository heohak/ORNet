package com.demo.bait.service.UserService;

import com.demo.bait.components.UserDetails;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;


@Service
public class LdapUserService {

    private final LdapTemplate ldapTemplate;

    public LdapUserService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public UserDetails getUserDetails(String username) {
        LdapQuery query = query()
                .base("ou=users")
//                .base("CN=Users,DC=bait,DC=local")
                .filter("(uid=" + username + ")");

        return ldapTemplate.findOne(query, UserDetails.class);
    }
}
