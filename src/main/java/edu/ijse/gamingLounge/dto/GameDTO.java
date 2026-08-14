package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {
    private Long id;

    @NotBlank(message = "Game name is required")
    private String gameName;

    @NotBlank(message = "Genre is required")
    private String genre;

    // Age suitability rating for the game, e.g. "T" (Teen 13+), "A" (Adult 20+)
    @NotBlank(message = "Age rating is required")
    @Pattern(regexp = "^[TA]$", message = "Age rating must be T=Teen or A=Adult")
    private String ageRating;
}
