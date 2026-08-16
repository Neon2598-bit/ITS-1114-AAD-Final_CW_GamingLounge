package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationTypeDTO {
    private Long id;

    @NotBlank(message = "Type name is required")
    private String typeName;

    @NotNull(message = "Hourly rate is required")
    @Positive(message = "Hourly rate must be greater than 0")
    private Double hourlyRate;
}
