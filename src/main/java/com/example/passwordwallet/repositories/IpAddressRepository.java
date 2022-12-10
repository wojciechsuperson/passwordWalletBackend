package com.example.passwordwallet.repositories;

import com.example.passwordwallet.entities.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {
    IpAddress findByAddress(String ipAddress);
}
