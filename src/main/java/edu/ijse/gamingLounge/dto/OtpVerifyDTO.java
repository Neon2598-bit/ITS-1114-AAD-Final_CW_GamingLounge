package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerifyDTO {
    @NotNull(message = "Customer is required")
    private Long customerId;

    @NotBlank(message = "OTP code is required")
    private String otpCode;
}
