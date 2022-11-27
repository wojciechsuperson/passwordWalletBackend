package com.example.passwordwallet.repositories;

import com.example.passwordwallet.entities.AppUser;
import com.example.passwordwallet.entities.Password;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<Password, Long> {
    List<Password> findAllByAppUser(AppUser appUser);
    Password findPasswordById(Long id);
}
