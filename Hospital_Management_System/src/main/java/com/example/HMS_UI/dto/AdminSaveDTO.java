package com.example.Hospital_Management_System.dto;

import com.example.Hospital_Management_System.constant.USER_ROLE;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO for creating a new admin user")
public class AdminSaveDTO {

    @Schema(
            description = "Full name of the admin",
            example = "Sandesh Adhikari"
    )
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Schema(
            description = "Admin's email address. Must be unique and properly formatted.",
            example = "sandesh12adk@gmail.com"
    )
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(
            description = "Secure password for admin account. Must contain at least 1 uppercase letter, 1 digit, and 1 special character.",
            example = "Secure@123"
    )
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
    @Pattern(regexp = ".*[!@#$%^&*()].*", message = "Password must contain at least one special character")
    private String password;
}
