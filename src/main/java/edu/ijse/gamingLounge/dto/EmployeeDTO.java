package edu.ijse.gamingLounge.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must have 2-100 characters at least")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}", message = "Contact number should be within 10 numbers")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "There should be at least 8 characters for the password")
    private String password;
}
