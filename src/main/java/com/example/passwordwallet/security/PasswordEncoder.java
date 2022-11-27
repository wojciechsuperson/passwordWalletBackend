package com.example.passwordwallet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordEncoder {
    @Bean
    public Sha512PasswordEncoder sha512PasswordEncoder() {
        return new Sha512PasswordEncoder();
    }
    @Bean
    public HmacPasswordEncoder hmacPasswordEncoder() {
        return new HmacPasswordEncoder();
    }
}
