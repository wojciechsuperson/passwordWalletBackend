package com.example.passwordwallet.contollers;

import com.example.passwordwallet.dto.*;
import com.example.passwordwallet.entities.AppUser;
import com.example.passwordwallet.entities.Password;
import com.example.passwordwallet.services.WalletService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class WalletController {
    private WalletService walletService;

    @GetMapping("/")
    public String home() {
        return "Hello World";
    }

    @GetMapping("users")
    public List<AppUser> getAllUsers(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        return this.walletService.getAllUsers();
    }

    @PostMapping("save_password")
    public String saveNewPass(@RequestBody NewPasswordToWalletDTO newPasswordToWalletDTO) {
        return this.walletService.saveNewPass(newPasswordToWalletDTO);
    }

    @PostMapping("get_password/{id}")
    public String getPasswordById(@PathVariable Long id,@RequestBody UserVerifyDTO userVerifyDTO) {
        return this.walletService.getPasswordWithId(userVerifyDTO,id);
    }

    @PostMapping("get_passwords")
    public List<Password> getAllUserPasswords(@RequestBody UserVerifyDTO userVerifyDTO) {
        return this.walletService.getAllUserPasswords(userVerifyDTO);
    }

}
