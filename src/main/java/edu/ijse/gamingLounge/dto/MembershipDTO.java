package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembershipDTO {
    private Long id;

    @NotNull(message = "Customer is required")
    private Long customerId;

    @NotNull(message = "Membership plan is required")
    private Long membershipPlanId;

    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    private String customerName;
    private String planName;
}
