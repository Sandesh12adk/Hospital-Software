package com.example.HMS_UI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO for user login credentials")
public class LoginSaveDTO {
    @Schema(description = "Email used for login", example = "sandesh12adk@gmail.com")
    private String email; // Changed from userName

    @Schema(description = "User's password", example = "YourPassword@123")
    private String password;
}
