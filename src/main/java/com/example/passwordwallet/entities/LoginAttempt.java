package com.example.passwordwallet.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_attempts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LoginAttempt {

    public LoginAttempt(LocalDateTime time, Boolean correct, IpAddress ipAddress, AppUser appUser) {
        this.time = time;
        this.correct = correct;
        this.ipAddress = ipAddress;
        this.appUser = appUser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "la_sequence")
    @SequenceGenerator(name = "la_sequence", sequenceName = "la_sequence", allocationSize = 1)
    private Long id;
    private LocalDateTime time;
    private Boolean correct;
    @ManyToOne
    @JoinColumn(name = "user_iduser", nullable = false)
    private AppUser appUser;
    @ManyToOne
    @JoinColumn(name = "ipaddress_id", nullable = false)
    private IpAddress ipAddress;
}