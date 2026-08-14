package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDTO {

    private Long id;

    @NotBlank(message = "Please Provide An Branch Name...")
    private String branchName;

    @NotBlank(message = "Please Provide An Address For The Branch...")
    private String address;

    @NotBlank(message = "Please Provide Contact Number...")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number should be 10 digits")
    private String contactNumber;
}
