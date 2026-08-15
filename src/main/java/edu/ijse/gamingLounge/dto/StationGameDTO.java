package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationGameDTO {
    private Long id;

    @NotNull(message = "Station is required")
    private Long stationId;

    @NotNull(message = "Game is required")
    private Long gameId;

    // Read-only, filled in when returning data
    private String stationCode; // PS-01, XB-01, PC-01 (PS5, Xbox, PC)
    private String gameName;
}
