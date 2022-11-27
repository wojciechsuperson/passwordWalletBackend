package com.example.passwordwallet.repositories;

import com.example.passwordwallet.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findAppUserByUsername(String login);
    Optional<AppUser> findAppUserByUsernameAndToken(String login, String token);
}
