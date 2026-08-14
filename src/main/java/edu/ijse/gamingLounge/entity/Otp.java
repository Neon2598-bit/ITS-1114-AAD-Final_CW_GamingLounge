package edu.ijse.gamingLounge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String otpCode;
    private String purpose;
    private LocalDateTime expiryTime;
    private Boolean verified;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
