package com.loginsocial.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.loginsocial.persistence.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTest {

    @LocalServerPort
    private Integer PORT;

    private String HOST;

    @Autowired
    protected TestRestTemplate restTemplate;

    @BeforeEach
    public void init(){
        HOST = "http://localhost:" + PORT;
    }

    @Test
    @DisplayName("deve executar o login do usuário com sucesso")
    void shouldPerformLogin_thenSuccess() throws URISyntaxException, JsonProcessingException {

        URI uri = new URI(HOST + "/v1/login");

        var login = new User(null, "testone@email.com", "12345678");

        HttpEntity<Object> request = new HttpEntity<>(login, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertFalse(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("deve tentar executar o login do usuário com senha incorreta")
    void shouldTryPerformLoginWithIncorrectPassword_thenFail() throws URISyntaxException, JsonProcessingException {

        URI uri = new URI(HOST + "/v1/login");

        var login = new User(null, "testone@email.com", "1234567111");

        HttpEntity<Object> request = new HttpEntity<>(login, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
        Assertions.assertNull(response.getBody());
    }
}