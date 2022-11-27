package com.example.passwordwallet;

import com.example.passwordwallet.dto.UserVerifyDTO;
import com.example.passwordwallet.entities.AppUser;
import com.example.passwordwallet.entities.Password;
import com.example.passwordwallet.repositories.PasswordRepository;
import com.example.passwordwallet.repositories.UserRepository;
import com.example.passwordwallet.services.AuthorizationService;
import com.example.passwordwallet.services.WalletService;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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
    void shouldReturnWhen(){
        //given
        AppUser appUser = AppUser.builder()
                .username("asd").isPasswordKeptAsHash(true).password("asdsad").token("asdsad")
                .salt("mojaSol")
                .id(1000L)
                .build();
        UserVerifyDTO userVerifyDTO = new UserVerifyDTO("test", "test");
        Password pass = Password.builder().password("gM4+mqQhGCI92ImnZwGAlA==").build();

        doReturn(Optional.of(appUser)).when(userRepository)
                .findAppUserByUsernameAndToken(anyString(), anyString());
        doReturn(Optional.of(appUser))
                .when(userRepository).findById(appUser.getId());
        doReturn(pass)
                .when(passwordRepository).findPasswordById(1L);
        //when
        String res = this.walletService.getPasswordWithId(userVerifyDTO, 1L);

        //then
        assertThat(res).isEqualTo("test");
    }
}
