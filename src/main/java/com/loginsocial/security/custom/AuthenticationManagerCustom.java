package com.loginsocial.security.custom;


import com.loginsocial.model.Login;
import com.loginsocial.persistence.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationManagerCustom implements AuthenticationManager {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        var username = authentication.getPrincipal() + "";
        var password = authentication.getCredentials() + "";

        UserDetails user = loadUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        if (!user.isEnabled()) {
            throw new DisabledException("User account is not active");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    public UserDetails loadUserByUsername(String username) {
        //@formatter:off
        Optional<Login> login = loginRepository.findByUsername(username);

        if (login.isEmpty())
            throw new UsernameNotFoundException(username);

        return new Login(login.get().getId(), login.get().getUsername(), login.get().getPassword());
        //@formatter:on
    }
}
