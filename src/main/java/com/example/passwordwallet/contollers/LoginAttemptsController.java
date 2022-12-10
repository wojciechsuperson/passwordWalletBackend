package com.example.passwordwallet.contollers;

import com.example.passwordwallet.dto.BadAndGoodLoginAttemptDTO;
import com.example.passwordwallet.dto.UserVerifyDTO;
import com.example.passwordwallet.services.LoginAttemptService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class LoginAttemptsController {
    private LoginAttemptService loginAttemptService;

    @PostMapping("login_attempts")
    public BadAndGoodLoginAttemptDTO getAllUserPasswords(@RequestBody UserVerifyDTO userVerifyDTO) {
        return this.loginAttemptService.getLastBadAndGoodLoginAttempt(userVerifyDTO);
    }
}
