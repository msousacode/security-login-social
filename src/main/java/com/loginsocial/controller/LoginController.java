package com.loginsocial.controller;

import com.loginsocial.model.Login;
import com.loginsocial.persistence.LoginRepository;
import com.loginsocial.security.custom.CustomAuthenticationManager;
import com.loginsocial.security.jwt.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

    private final CustomAuthenticationManager customAuthenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginRepository loginRepository;

    public LoginController(
            CustomAuthenticationManager customAuthenticationManager,
            JwtTokenProvider jwtTokenProvider,
            LoginRepository loginRepository) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginRepository = loginRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        var authentication = customAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        return ResponseEntity.ok().body(jwtTokenProvider.genereToken(authentication));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        return jwtTokenProvider.validateToken(token) ? ResponseEntity.ok().body(Boolean.TRUE) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Boolean.FALSE);
    }
}
