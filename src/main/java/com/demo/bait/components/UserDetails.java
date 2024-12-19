package com.demo.bait.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;


@Getter
@Setter
@Entry(
//        base = "ou=users,dc=example,dc=com", // Base DN where the user entries are located
        base = "OU=SBSUsers,OU=Users,OU=MyBusiness,DC=bait,DC=local",
        objectClasses = {"person", "organizationalPerson", "inetOrgPerson"} // LDAP object classes
)
public class UserDetails {

    @Id // The DN of the entry
    @JsonIgnore
    private Name dn;

    @Attribute(name = "uid") // Map to the "uid" attribute in LDAP
    private String username;

    @Attribute(name = "cn") // Map to the "cn" attribute in LDAP
    private String cn;

    @Attribute(name = "sn") // Map to the "sn" attribute in LDAP
    private String sn;

    @Attribute(name = "mail") // Map to the "mail" attribute in LDAP
    private String mail;
}
