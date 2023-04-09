package com.loginsocial.service;

import com.loginsocial.payload.LoginRequest;
import com.loginsocial.security.CustomAuthenticationManager;
import com.loginsocial.security.TokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CustomAuthenticationManager customAuthenticationManager;
    private final TokenProvider jwtTokenProvider;

    public AuthService(
            CustomAuthenticationManager customAuthenticationManager,
            TokenProvider jwtTokenProvider) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String authenticationUser(LoginRequest loginRequest) {

        var authentication = customAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.createToken(authentication);
    }

    public Boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
}
