package com.loginsocial.controller;

import com.loginsocial.persistence.entity.User;
import com.loginsocial.persistence.repository.UserRepository;
import com.loginsocial.security.CurrentUser;
import com.loginsocial.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    public ResponseEntity<User> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        if (userRepository.findById(userPrincipal.getId()).isPresent()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
