package com.example.passwordwallet.services;

import com.example.passwordwallet.dto.AuthDTO;
import com.example.passwordwallet.dto.ChangePasswordDTO;
import com.example.passwordwallet.dto.RegistrationDTO;
import com.example.passwordwallet.dto.UserVerifyDTO;
import com.example.passwordwallet.entities.AppUser;
import com.example.passwordwallet.entities.IpAddress;
import com.example.passwordwallet.entities.LoginAttempt;
import com.example.passwordwallet.repositories.IpAddressRepository;
import com.example.passwordwallet.repositories.LoginAttemptRepository;
import com.example.passwordwallet.repositories.UserRepository;
import com.example.passwordwallet.security.HmacPasswordEncoder;
import com.example.passwordwallet.security.Sha512PasswordEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthorizationService implements UserDetailsService {


    private final static String USER_NOT_FOUND_MSG = "user with login %s not found";
    private final static Integer MAX_IP_ADDRESS_BAD_ATTEMPTS_IN_ROW = 3;
    private final UserRepository userRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final IpAddressRepository ipAddressRepository;
    private final Sha512PasswordEncoder sha512PasswordEncoder;
    private final HmacPasswordEncoder hmacPasswordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
    }

    public String register(RegistrationDTO registrationDTO) {
        boolean userExist = userRepository.findAppUserByUsername(registrationDTO.getLogin()).isPresent();
        if (userExist) {
            throw new IllegalStateException("login already taken");
        }
        String salt = sha512PasswordEncoder.getNextSalt();
        String encodedPassword = encodePassword(registrationDTO, salt);
        AppUser appUser = new AppUser(registrationDTO.getLogin(), encodedPassword);
        appUser.setSalt(salt);
        appUser.setPasswordKeptAsHash(registrationDTO.getIsPasswordKeptAsHash());
        userRepository.save(appUser);
        return "user registered!";
    }

    private String encodePassword(RegistrationDTO registrationDTO, String salt) {
        String encodedPassword;
        if (registrationDTO.getIsPasswordKeptAsHash()) {
            encodedPassword = sha512PasswordEncoder.encode(salt + registrationDTO.getPassword());
        } else {
            encodedPassword = hmacPasswordEncoder.calculateHMAC(registrationDTO.getPassword(), salt);
        }
        return encodedPassword;
    }

    public String login(AuthDTO authDTO, String ipAddress) {
        AppUser userByLogin = userRepository.findAppUserByUsername(authDTO.getLogin()).orElse(null);
        checkCredentials(authDTO, userByLogin, ipAddress);

        // if we are here credentials are ok
        setIpAddressGoodAttempt(ipAddress, userByLogin);

        String token = UUID.randomUUID().toString();
        userByLogin.setToken(token);
        userRepository.save(userByLogin);
        return token;
    }

    private void setIpAddressGoodAttempt(String ipAddressFromRequest, AppUser user) {
        IpAddress ipAddress = ipAddressRepository.findByAddress(ipAddressFromRequest);
        if (ipAddress == null) {
            ipAddress = new IpAddress(ipAddressFromRequest, 0);
        } else {
            ipAddress.setBadLoginAttemptsInRow(0);
        }
        ipAddressRepository.save(ipAddress);

        LoginAttempt loginAttempt = new LoginAttempt(LocalDateTime.now(), true, ipAddress, user);
        loginAttemptRepository.save(loginAttempt);
    }

    private void checkCredentials(AuthDTO authDTO, AppUser userByLogin, String ipAddressFromRequest) {
        if (userByLogin == null) {
            throw new IllegalStateException("Wrong login!");
        }
        if(userByLogin.getLockoutTime() != null && LocalDateTime.now().isBefore(userByLogin.getLockoutTime())) {
            throw new IllegalStateException("Your account is blocked till: " + userByLogin.getLockoutTime());
        }
        if ((userByLogin.isPasswordKeptAsHash() && !sha512PasswordEncoder.matches(userByLogin.getSalt() + authDTO.getPassword(), userByLogin.getPassword()))
                || (!userByLogin.isPasswordKeptAsHash() && !hmacPasswordEncoder.matches(authDTO.getPassword(), userByLogin.getPassword(), userByLogin.getSalt()))) {
            setIpAddressBadAttempt(ipAddressFromRequest, userByLogin);
            userByLogin.setLockoutTime(LocalDateTime.now().plus(1, ChronoUnit.MINUTES));
            userByLogin.setIncorrectAttemptsCounter(userByLogin.getIncorrectAttemptsCounter() != null ? userByLogin.getIncorrectAttemptsCounter() + 1 : 1);
            userRepository.save(userByLogin);
            throw new IllegalStateException("Wrong password!");
        }
        IpAddress byAddress = ipAddressRepository.findByAddress(ipAddressFromRequest);
        if (byAddress != null && byAddress.getBadLoginAttemptsInRow() >= MAX_IP_ADDRESS_BAD_ATTEMPTS_IN_ROW) {
            throw new IllegalStateException("Your IP is blocked!");
        }
    }

    private void checkCredentials(AuthDTO authDTO, AppUser userByLogin) {
        if (userByLogin == null) {
            throw new IllegalStateException("Wrong login!");
        }
        if ((userByLogin.isPasswordKeptAsHash() && !sha512PasswordEncoder.matches(userByLogin.getSalt() + authDTO.getPassword(), userByLogin.getPassword()))
                || (!userByLogin.isPasswordKeptAsHash() && !hmacPasswordEncoder.matches(authDTO.getPassword(), userByLogin.getPassword(), userByLogin.getSalt()))) {
            throw new IllegalStateException("Wrong password!");
        }
    }

    private void setIpAddressBadAttempt(String ipAddressFromRequest, AppUser userByLogin) {
        IpAddress ipAddress = ipAddressRepository.findByAddress(ipAddressFromRequest);
        if (ipAddress == null) {
            ipAddress = new IpAddress(ipAddressFromRequest, 1);
        }
        if (ipAddress.getBadLoginAttemptsInRow() >= MAX_IP_ADDRESS_BAD_ATTEMPTS_IN_ROW) {
            throw new IllegalStateException("Your IP is blocked");
        } else {
            ipAddress.setBadLoginAttemptsInRow(ipAddress.getBadLoginAttemptsInRow() + 1);
        }
        ipAddressRepository.save(ipAddress);

        LoginAttempt loginAttempt = new LoginAttempt(LocalDateTime.now(), false, ipAddress, userByLogin);
        loginAttemptRepository.save(loginAttempt);
    }

    public String logout(UserVerifyDTO userVerifyDTO) {
        AppUser userByLogin = userRepository.findAppUserByUsername(userVerifyDTO.getLogin()).orElse(null);
        if (userByLogin == null) {
            throw new IllegalStateException("There is no such user to logout!");
        }
        userByLogin.setToken(null);
        userRepository.save(userByLogin);
        return "logged out!";
    }

    public String changePassword(ChangePasswordDTO changePasswordDTO) {
        AppUser userByLogin = userRepository.findAppUserByUsername(changePasswordDTO.getLogin()).orElse(null);
        checkCredentials(changePasswordDTO, userByLogin);

        String salt = sha512PasswordEncoder.getNextSalt();
        userByLogin.setSalt(salt);

        if (changePasswordDTO.getIsPasswordKeptAsHash()) {
            userByLogin.setPassword(sha512PasswordEncoder.encode(salt + changePasswordDTO.getNewPassword()));
        } else {
            userByLogin.setPassword(hmacPasswordEncoder.calculateHMAC(changePasswordDTO.getNewPassword(), salt));
        }

        userRepository.save(userByLogin);
        return "password changed!";
    }
}
