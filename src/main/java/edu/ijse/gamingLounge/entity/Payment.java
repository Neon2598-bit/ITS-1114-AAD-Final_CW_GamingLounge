package edu.ijse.gamingLounge.entity;

import edu.ijse.gamingLounge.status.PaymentMethod;
import edu.ijse.gamingLounge.status.PaymentStatus;
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
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paymentDate;

    /*WHAT this payment was for (e.g. "BOOKING", "FOOD_ORDER",
    "MEMBERSHIP")
     */
    private String paymentFor;
    private Long referenceId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
