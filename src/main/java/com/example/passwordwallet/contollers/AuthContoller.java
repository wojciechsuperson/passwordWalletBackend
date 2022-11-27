package com.example.passwordwallet.contollers;

import com.example.passwordwallet.dto.AuthDTO;
import com.example.passwordwallet.dto.ChangePasswordDTO;
import com.example.passwordwallet.dto.RegistrationDTO;
import com.example.passwordwallet.dto.UserVerifyDTO;
import com.example.passwordwallet.services.AuthorizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class AuthContoller {
    private AuthorizationService authorizationService;

    @PostMapping("registration")
    public ResponseEntity<String> register(@RequestBody RegistrationDTO registrationDTO) {
        return ResponseEntity.ok(authorizationService.register(registrationDTO));
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody AuthDTO authDTO) {
        return ResponseEntity.ok(authorizationService.login(authDTO));
    }

    @PostMapping("log_out")
    public String logout(@RequestBody UserVerifyDTO userVerifyDTO) {
        return authorizationService.logout(userVerifyDTO);
    }

    @PostMapping("change_password")
    public String changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return authorizationService.changePassword(changePasswordDTO);
    }
}
