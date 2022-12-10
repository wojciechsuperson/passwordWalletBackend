package com.example.passwordwallet.contollers;

import com.example.passwordwallet.dto.AuthDTO;
import com.example.passwordwallet.dto.ChangePasswordDTO;
import com.example.passwordwallet.dto.RegistrationDTO;
import com.example.passwordwallet.dto.UserVerifyDTO;
import com.example.passwordwallet.services.AuthorizationService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;
import java.net.InetAddress;

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

    @SneakyThrows
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody AuthDTO authDTO, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        if(request == null || "0:0:0:0:0:0:0:1".equals(ipAddress) || "127.0.0.1".equals(ipAddress)) {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ipAddress = inetAddress.getHostAddress();
        }
        return ResponseEntity.ok(authorizationService.login(authDTO, ipAddress));
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
