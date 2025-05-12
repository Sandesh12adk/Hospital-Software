package com.example.Hospital_Management_System.dto;

import com.example.Hospital_Management_System.constant.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DoctorSaveDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
    @Pattern(regexp = ".*[!@#$%^&*()].*", message = "Password must contain at least one special character")
    private String password;

    @JsonIgnore
    private USER_ROLE role;

    @NotBlank(message = "Specialization cannot be blank")
    private String specialization;

    @Min(value = 1, message = "Department ID must be a positive number")
    private int departmentId;

    public DoctorSaveDTO(){
        role=USER_ROLE.DOCTOR;
    }
}
