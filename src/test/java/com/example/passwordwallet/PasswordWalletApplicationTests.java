package com.example.passwordwallet;

import com.example.passwordwallet.dto.UserVerifyDTO;
import com.example.passwordwallet.entities.AppUser;
import com.example.passwordwallet.entities.Password;
import com.example.passwordwallet.repositories.PasswordRepository;
import com.example.passwordwallet.repositories.UserRepository;
import com.example.passwordwallet.services.WalletService;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

class PasswordWallerApplicationTest implements WithAssertions {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordRepository passwordRepository;

    @InjectMocks
    private WalletService walletService;

    private AutoCloseable closeable;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void shouldDecryptPasswordForUserWithHisSalt() {
        //given
        AppUser appUser = AppUser.builder()
                .salt("GMUu4cmD0++eToW2QiJ4vg==")
                .id(1000L)
                .build();
        UserVerifyDTO userVerifyDTO = new UserVerifyDTO("test", "test");
        Password pass = Password.builder().password("+jrYQ84xj1OIfhnYAss/Qw==").build();

        doReturn(Optional.of(appUser)).when(userRepository)
                .findAppUserByUsernameAndToken(anyString(), anyString());
        doReturn(pass)
                .when(passwordRepository).findPasswordById(1L);
        //when
        String resPassword = this.walletService.getPasswordWithId(userVerifyDTO, 1L);

        //then
        assertThat(resPassword).isNotNull();
        assertThat(resPassword).isEqualTo("testowe");
    }

    @Test
    void shouldReturnExactNumberOfUserPassword() {
        //given
        AppUser appUser = AppUser.builder()
                .id(1000L)
                .build();
        UserVerifyDTO userVerifyDTO = new UserVerifyDTO("test", "test");
        Password pass = Password.builder().password("+jrYQ84xj1OIfhnYAss/Qw==").build();

        doReturn(Optional.of(appUser)).when(userRepository)
                .findAppUserByUsernameAndToken(anyString(), anyString());
        doReturn(List.of(pass,pass,pass))
                .when(passwordRepository).findAllByAppUser(appUser);
        //when
        List<Password> resPasswordList = this.walletService.getAllUserPasswords(userVerifyDTO);

        //then
        assertThat(resPasswordList).isNotNull();
        assertThat(resPasswordList.size()).isEqualTo(3);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenBadSaltGiven() {
        //given
        AppUser appUser = AppUser.builder()
                .salt("zaKrotkaSol")
                .id(1000L)
                .build();
        UserVerifyDTO userVerifyDTO = new UserVerifyDTO("test", "test");
        Password pass = Password.builder().password("+jrYQ84xj1OIfhnYAss/Qw==").build();

        doReturn(Optional.of(appUser)).when(userRepository)
                .findAppUserByUsernameAndToken(anyString(), anyString());
        doReturn(pass)
                .when(passwordRepository).findPasswordById(1L);
        //when
        //then
        assertThatThrownBy(() -> {this.walletService.getPasswordWithId(userVerifyDTO, 1L);})
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenBadEncodedPassGiven() {
        //given
        AppUser appUser = AppUser.builder()
                .salt("GMUu4cmD0++eToW2QiJ4vg==")
                .id(1000L)
                .build();
        UserVerifyDTO userVerifyDTO = new UserVerifyDTO("test", "test");
        Password pass = Password.builder().password("jakiesBledneHaslo").build();

        doReturn(Optional.of(appUser)).when(userRepository)
                .findAppUserByUsernameAndToken(anyString(), anyString());
        doReturn(pass)
                .when(passwordRepository).findPasswordById(1L);
        //when
        //then
        assertThatThrownBy(() -> {this.walletService.getPasswordWithId(userVerifyDTO, 1L);})
                .isInstanceOf(IllegalArgumentException.class);
    }

}
