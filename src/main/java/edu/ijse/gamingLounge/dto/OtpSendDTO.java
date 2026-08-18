package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpSendDTO {
    @NotNull(message = "Customer is required")
    private Long customerId;
}
