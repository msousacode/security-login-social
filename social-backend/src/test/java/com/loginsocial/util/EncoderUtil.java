package com.loginsocial.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderUtil {

    static String encode() {
        return new BCryptPasswordEncoder().encode("12345678");
    }

    public static void main(String[] args) {
        System.out.println(encode());
    }
}
