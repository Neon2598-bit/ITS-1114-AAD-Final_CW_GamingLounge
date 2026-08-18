package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long id;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    private String status;

    private Double totalAmount;

    @NotNull(message = "Customer is required")
    private Long customerId;

    @NotNull(message = "Station is required")
    private Long stationId;

    private String customerName;
    private String stationCode;
}
