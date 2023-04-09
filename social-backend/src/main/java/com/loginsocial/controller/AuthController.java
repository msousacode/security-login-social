package com.loginsocial.controller;

import com.loginsocial.payload.LoginRequest;
import com.loginsocial.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthService loginService;

    public AuthController(AuthService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticationUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(loginService.authenticationUser(loginRequest));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        return loginService.validateToken(token) ? ResponseEntity.ok().body(Boolean.TRUE) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Boolean.FALSE);
    }
}
