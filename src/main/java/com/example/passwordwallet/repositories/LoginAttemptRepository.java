package com.example.passwordwallet.repositories;

import com.example.passwordwallet.entities.AppUser;
import com.example.passwordwallet.entities.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    List<LoginAttempt> findAllByAppUserOrderByTimeDesc(AppUser appUser);
}
