package edu.ijse.gamingLounge.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodOrderDTO {
    private Long id;

    private LocalDateTime orderDate;
    private String status;
    private Double totalAmount;

    @NotNull(message = "Customer is required")
    private Long customerId;
    private String customerName;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<FoodOrderItemDTO> items;
}
