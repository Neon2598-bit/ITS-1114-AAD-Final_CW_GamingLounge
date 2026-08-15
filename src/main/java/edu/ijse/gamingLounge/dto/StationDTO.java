package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationDTO {
    private Long id;

    @NotBlank(message = "Station code is required")
    private String stationCode; // PS-01, XB-01, PC-01 (PS5, Xbox, PC)

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Branch is required")
    private Long branchId;

    @NotNull(message = "Station type is required")
    private Long stationTypeId;
    
    private String branchName;
    private String typeName;
    private Double hourlyRate;
}
