package com.example.passwordwallet.dto;

import lombok.Data;

@Data
public class NewPasswordToWalletDTO extends UserVerifyDTO {
    private String username;
    private String password;
    private String webAddress;
}
