package com.example.passwordwallet.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "password")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Password {

    public Password(String webAddress, String username, String password, AppUser appUser) {
        this.webAddress = webAddress;
        this.username = username;
        this.password = password;
        this.appUser = appUser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_sequence")
    @SequenceGenerator(name = "password_sequence", sequenceName = "password_sequence", allocationSize = 1)
    private Long id;
    private String webAddress;
    private String username;
    private String password;
    @ManyToOne
    @JoinColumn(name = "user_iduser", nullable = false)
    private AppUser appUser;
}
