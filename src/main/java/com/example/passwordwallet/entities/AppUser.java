package com.example.passwordwallet.entities;

import com.example.passwordwallet.models.UserStatus;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AppUser implements UserDetails {

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.status = UserStatus.OK;
    }
    public AppUser(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.status = UserStatus.OK;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    private Long id;
    private String username;
    private String password;
    private String salt;
    private boolean isPasswordKeptAsHash;
    @Nullable
    private String token;
    private UserStatus status;
    private LocalDateTime lockoutTime;
    private Integer incorrectAttemptsCounter;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
