package com.loginsocial.security.custom;


import com.loginsocial.persistence.entity.UserPrincipal;
import com.loginsocial.persistence.repository.LoginRepository;
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
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

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
        Optional<UserPrincipal> login = loginRepository.findByUsername(username);

        if (login.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return new UserPrincipal(login.get().getId(), login.get().getUsername(), login.get().getPassword());
        //@formatter:on
    }
}
