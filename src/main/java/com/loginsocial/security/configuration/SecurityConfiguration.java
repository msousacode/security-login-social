package com.loginsocial.security.configuration;

import com.loginsocial.security.custom.AuthenticationManagerCustom;
import com.loginsocial.security.jwt.JwtTokenAuthenticationFilter;
import com.loginsocial.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

        @Autowired
        private AuthenticationManagerCustom authenticationManager;

        @Bean
        protected SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider tokenProvider) throws Exception {
            //@formatter:off
            return http
                    .cors().and()
                    .csrf().disable()
                    .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeRequests(c -> c
                            .antMatchers(HttpMethod.POST, "/v1/login").permitAll()
                            .antMatchers(HttpMethod.POST, "/v1/authentication/token").permitAll()
                            .antMatchers(HttpMethod.GET, "/v1/authentication/token/**").permitAll()
                            .antMatchers(HttpMethod.POST, "/v1/account/signup").permitAll()
                            .anyRequest().authenticated()
                    )
                    .authenticationManager(authenticationManager)
                    .addFilterBefore(new JwtTokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                    .build();
            //@formatter:on
        }
}
