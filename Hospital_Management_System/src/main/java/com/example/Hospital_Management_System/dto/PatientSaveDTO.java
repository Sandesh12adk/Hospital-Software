package com.example.Hospital_Management_System.dto;

import com.example.Hospital_Management_System.constant.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO for saving patient registration details")
public class PatientSaveDTO {

    @NotBlank(message = "Name cannot be blank")
    @Schema(description = "Full name of the patient", example = "Sita Tamang")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Schema(description = "Email address of the patient", example = "sita.tamang@gmail.com")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
    @Pattern(regexp = ".*[!@#$%^&*()].*", message = "Password must contain at least one special character")
    @Schema(
            description = "Secure password for the patient account. Must contain 1 uppercase letter, 1 digit, and 1 special character.",
            example = "Sita@2025"
    )
    private String password;

    @JsonIgnore
    @Schema(hidden = true)
    private USER_ROLE role;

    @NotBlank(message = "Gender must not be blank")
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
    @Schema(description = "Gender of the patient", example = "Female")
    private String gender;

    @Min(value = 0, message = "Age must be zero or a positive number")
    @Max(value = 150, message = "Age must be a realistic number")
    @Schema(description = "Age of the patient", example = "28")
    private int age;
}
