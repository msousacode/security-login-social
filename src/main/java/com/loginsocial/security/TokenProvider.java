package com.loginsocial.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    @Value("${app.auth.tokenExpirationMsec}")
    private long tokenExpirationMsec;

    public String createToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date expiryDate = Date.from(Instant.now().plus(Duration.ofSeconds(tokenExpirationMsec)));
        SecretKey secretKey = Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(userPrincipal.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {

        SecretKey secretKey = Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8));

        Claims body = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        String username = body.getSubject();
        List<GrantedAuthority> authorities = getRoles(body);

        return new UsernamePasswordAuthenticationToken(username, token, authorities);
    }

    private List<GrantedAuthority> getRoles(Claims body) {
        Collection<?> roles = body.get("roles", Collection.class);
        if (roles != null) {
            return roles.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.toString()))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public boolean validateToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8));
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (RuntimeException ex) {
        }
        return false;
    }
}
