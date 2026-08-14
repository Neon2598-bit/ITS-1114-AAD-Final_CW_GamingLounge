package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembershipPlanDTO {
    private Long id;

    @NotBlank(message = "Plan name is required")
    private String planName;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be greater than 0")
    private Integer durationDays;

    @NotNull(message = "Discount percentage is required")
    @Min(value = 0, message = "Discount cannot be less than 0")
    @Max(value = 100, message = "Discount cannot be more than 100")
    private Double discountPercentage;
}
