package com.example.passwordwallet.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ip_address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class IpAddress {

    public IpAddress(String address, Integer badLoginAttemptsInRow) {
        this.address = address;
        this.badLoginAttemptsInRow = badLoginAttemptsInRow;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ip_sequence")
    @SequenceGenerator(name = "ip_sequence", sequenceName = "ip_sequence", allocationSize = 1)
    private Long id;
    private String address;
    private Integer badLoginAttemptsInRow;
}