package com.example.passwordwallet.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BadAndGoodLoginAttemptDTO {
    LocalDateTime lastGoodLoginAttempt;
    LocalDateTime lastBadLoginAttempt;
}
