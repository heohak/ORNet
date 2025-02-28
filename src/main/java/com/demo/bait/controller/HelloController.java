package com.demo.bait.controller;

import com.demo.bait.components.UserDetails;
import com.demo.bait.service.UserService.LdapUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@AllArgsConstructor
@RestController
public class HelloController {

    private LdapUserService ldapUserService;

    @RequestMapping("/")
    public String index() {
        return "Hello World";
    }

    @GetMapping("/api/admin/dashboard")
    public ResponseEntity<?> getAdminDashboard() {
        // Get authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName(); // Username
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities(); // Roles

        System.out.println("Logged in user: " + username);
        System.out.println("Roles: " + roles);

        return ResponseEntity.ok("Welcome to the Admin Dashboard, " + username + "!");
    }

    @GetMapping("/api/profile")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

        System.out.println("Logged in user: " + username);
        System.out.println("Roles: " + roles);

        return ResponseEntity.ok("Here is your profile, " + username + "!" + " Roles: " + roles);
    }

    @GetMapping("/api/details")
    public UserDetails getUserDetails() {
        // Get the logged-in username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch user details from LDAP
//        return ldapUserService.getUserDetails(username);
        return ldapUserService.getUserDetails(username);
    }
}
