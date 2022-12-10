package com.example.passwordwallet.services;

import com.example.passwordwallet.dto.BadAndGoodLoginAttemptDTO;
import com.example.passwordwallet.dto.UserVerifyDTO;
import com.example.passwordwallet.entities.AppUser;
import com.example.passwordwallet.entities.LoginAttempt;
import com.example.passwordwallet.repositories.LoginAttemptRepository;
import com.example.passwordwallet.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LoginAttemptService {
    private UserRepository userRepository;
    private LoginAttemptRepository loginAttemptRepository;

    public BadAndGoodLoginAttemptDTO getLastBadAndGoodLoginAttempt(UserVerifyDTO userVerifyDTO) {
        AppUser appUser = this.userRepository
                .findAppUserByUsernameAndToken(userVerifyDTO.getLogin(), userVerifyDTO.getToken()).orElse(null);
        checkUserExistence(appUser);

        BadAndGoodLoginAttemptDTO response = new BadAndGoodLoginAttemptDTO();
        List<LoginAttempt> allUserLoginAttempts = loginAttemptRepository.findAllByAppUserOrderByTimeDesc(appUser);

        LoginAttempt lastBad = allUserLoginAttempts.stream().filter(a -> !a.getCorrect()).findFirst().orElse(null);
        LoginAttempt lastGood = allUserLoginAttempts.stream().filter(a -> a.getCorrect()).findFirst().orElse(null);

        response.setLastGoodLoginAttempt(lastGood == null ? null : lastGood.getTime());
        response.setLastBadLoginAttempt(lastBad == null ? null : lastBad.getTime());
        return response;
    }

    private void checkUserExistence(AppUser appUser) {
        if(appUser == null) {
            throw new IllegalStateException("Bad user credentials!");
        }
    }
}
