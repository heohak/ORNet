package com.demo.bait.service.UserService;

import com.demo.bait.components.UserDetails;
import com.demo.bait.dto.ResponseDTO;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static org.springframework.ldap.query.LdapQueryBuilder.query;


@Service
public class LdapUserService {

    private final LdapTemplate ldapTemplate;

    public LdapUserService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public UserDetails getUserDetails(String username) {
        LdapQuery query = query()
//                .base("OU=SBSUsers,OU=Users,OU=MyBusiness,DC=bait,DC=local")
//                .filter("(cn=" + username + ")");
                .base("ou=users")
                .filter("(uid=" + username + ")");

        return ldapTemplate.findOne(query, UserDetails.class);
    }

    public ResponseDTO getUserUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username != null && username.endsWith("@bait.local")) {
            username = username.substring(0, username.indexOf("@"));
        }
        return new ResponseDTO(username);
    }
}
