package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long id;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;   // "CASH", "CARD", "ONLINE"

    @NotBlank(message = "Payment purpose is required")
    private String paymentFor;      // "BOOKING", "FOOD_ORDER", or "MEMBERSHIP"

    @NotNull(message = "Reference id is required")
    private Long referenceId;       // the id of that booking / order / membership

    @NotNull(message = "Customer is required")
    private Long customerId;

    private String status;
    private LocalDateTime paymentDate;
    private String customerName;
}
