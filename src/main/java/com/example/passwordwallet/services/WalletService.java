package com.example.passwordwallet.services;
import com.example.passwordwallet.dto.NewPasswordToWalletDTO;
import com.example.passwordwallet.dto.UserVerifyDTO;
import com.example.passwordwallet.entities.AppUser;
import com.example.passwordwallet.entities.Password;
import com.example.passwordwallet.repositories.PasswordRepository;
import com.example.passwordwallet.repositories.UserRepository;
import com.example.passwordwallet.security.AESenc;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class WalletService {
    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;

    public List<AppUser> getAllUsers() {
       return this.userRepository.findAll();
    }

    public String saveNewPass(NewPasswordToWalletDTO newPasswordToWalletDTO) {
        AppUser appUser = this.userRepository
                .findAppUserByUsernameAndToken(newPasswordToWalletDTO.getLogin(), newPasswordToWalletDTO.getToken()).orElse(null);
        checkUserExistence(appUser);

        String encodedPass = "encoded";
        try{
            encodedPass = AESenc.encrypt(newPasswordToWalletDTO.getPassword(), AESenc.generateKey(appUser.getSalt()));
        } catch (Exception ignored) {}

        Password password = new Password(
                newPasswordToWalletDTO.getWebAddress(),
                newPasswordToWalletDTO.getUsername(),
                encodedPass,
                appUser
        );

        this.passwordRepository.save(password);
        return "password added!";
    }

    private void checkUserExistence(AppUser appUser) {
        if(appUser == null) {
            throw new IllegalStateException("Bad user credentials!");
        }
    }

    public String getPasswordWithId(UserVerifyDTO userVerifyDTO, Long id) {
        AppUser appUser = this.userRepository
                .findAppUserByUsernameAndToken(userVerifyDTO.getLogin(), userVerifyDTO.getToken()).orElse(null);
        checkUserExistence(appUser);

        String decrypted = "decrypted";
        String encryptedPass = this.passwordRepository.findPasswordById(id).getPassword();
        try{
             decrypted = AESenc.decrypt(encryptedPass, AESenc.generateKey(appUser.getSalt()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Incorrect password or salt to decode!");
        }

        return decrypted;
    }

    public List<Password> getAllUserPasswords(UserVerifyDTO userVerifyDTO){
        AppUser appUser = this.userRepository
                .findAppUserByUsernameAndToken(userVerifyDTO.getLogin(), userVerifyDTO.getToken()).orElse(null);
        checkUserExistence(appUser);
        return this.passwordRepository.findAllByAppUser(appUser);
    }
}
