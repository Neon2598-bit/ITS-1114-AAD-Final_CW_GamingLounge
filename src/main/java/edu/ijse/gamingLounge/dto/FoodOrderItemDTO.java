package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodOrderItemDTO {
    private Long id;

    @NotNull(message = "Snack is required")
    private Long snackId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

    private Double subTotal;

    private String snackName;
}
