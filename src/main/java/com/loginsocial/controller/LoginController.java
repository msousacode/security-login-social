package com.loginsocial.controller;

import com.loginsocial.persistence.entity.UserPrincipal;
import com.loginsocial.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(path = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserPrincipal login) {
        return ResponseEntity.ok().body(loginService.login(login));
    }

    @PostMapping("/oauth2/google")
    public ResponseEntity<String> loginGoogle(@RequestBody String token) throws IOException {
        var check = loginService.checkGoogleToken(token);
        return check.map(s -> ResponseEntity.ok().body(s)).orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        return loginService.validateToken(token) ? ResponseEntity.ok().body(Boolean.TRUE) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Boolean.FALSE);
    }
}
