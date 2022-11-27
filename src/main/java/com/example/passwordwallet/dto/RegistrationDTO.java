package com.example.passwordwallet.dto;

import lombok.Data;

@Data
public class RegistrationDTO extends AuthDTO {
    private Boolean isPasswordKeptAsHash;
}
