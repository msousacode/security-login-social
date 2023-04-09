package com.loginsocial.payload;

public record LoginRequest(
        String email,
        String password) {
}
