package com.example.Hospital_Management_System.dto;

import com.example.Hospital_Management_System.constant.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PatientSaveDTO {
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

    @NotBlank(message = "Gender must not be blank")
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
    private String gender;

    @Min(value = 0, message = "Age must be zero or a positive number")
    @Max(value = 150, message = "Age must be a realistic number")
    private int age;

    public PatientSaveDTO(){
        role=USER_ROLE.PATIENT;
    }
}
