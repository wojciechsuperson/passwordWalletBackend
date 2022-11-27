package com.example.passwordwallet.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO extends AuthDTO{
    private String newPassword;
    private Boolean isPasswordKeptAsHash;
}
