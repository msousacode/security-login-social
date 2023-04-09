package com.loginsocial.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.loginsocial.persistence.entity.UserPrincipal;
import com.loginsocial.persistence.repository.LoginRepository;
import com.loginsocial.security.custom.CustomAuthenticationManager;
import com.loginsocial.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Service
public class LoginService {

    private final CustomAuthenticationManager customAuthenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginRepository loginRepository;

    public LoginService(
            CustomAuthenticationManager customAuthenticationManager,
            JwtTokenProvider jwtTokenProvider,
            LoginRepository loginRepository) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginRepository = loginRepository;
    }

    @Value("${google.client}")
    private String googleClientId;

    @Value("${google.secret}")
    private String googleSecret;

    public String login(UserPrincipal login) {
        return performLogin(login);
    }

    public Optional<String> checkGoogleToken(String token) throws IOException {

        final var transport = new NetHttpTransport();
        final var jacksonFactory = JacksonFactory.getDefaultInstance();

        final var verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory).setAudience(Collections.singletonList(googleClientId));

        final var googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), token);
        final var payload = googleIdToken.getPayload();

        UserPrincipal login = new UserPrincipal();

        if (loginRepository.findByUsername(payload.getEmail()).isPresent()) {
            return Optional.of(performLogin(login));
        } else {
            return Optional.empty();
        }
    }

    public Boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    private String performLogin(UserPrincipal login) {
        var authentication = customAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.createToken(authentication);
    }
}
