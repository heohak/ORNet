package com.demo.bait.controller.UserController;

import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.service.UserService.LdapUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final LdapUserService ldapUserService;

    @GetMapping("/username")
    public ResponseDTO getUserUsername() {
        return ldapUserService.getUserUsername();
    }
}
