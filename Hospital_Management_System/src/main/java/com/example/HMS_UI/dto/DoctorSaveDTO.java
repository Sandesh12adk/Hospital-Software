package com.example.HMS_UI.dto;

import com.example.HMS_UI.constant.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "DTO for saving a doctor's account and details")
public class DoctorSaveDTO {

    @NotBlank(message = "Name cannot be blank")
    @Schema(description = "Full name of the doctor", example = "Dr. Ramesh Sharma")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Schema(description = "Email address of the doctor", example = "ramesh.sharma@hospital.com")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
    @Pattern(regexp = ".*[!@#$%^&*()].*", message = "Password must contain at least one special character")
    @Schema(
            description = "Secure password for doctor account. Must contain 1 uppercase letter, 1 digit, and 1 special character.",
            example = "Doctor@123"
    )
    private String password;

    @JsonIgnore
    @Schema(hidden = true)
    private USER_ROLE role;

    @NotBlank(message = "Specialization cannot be blank")
    @Schema(description = "Medical specialization of the doctor", example = "Neurology")
    private String specialization;

    @Min(value = 1, message = "Department ID must be a positive number")
    @Schema(description = "ID of the department the doctor belongs to", example = "3")
    private Integer departmentId;

    public DoctorSaveDTO(){
        role = USER_ROLE.DOCTOR;
    }
}
