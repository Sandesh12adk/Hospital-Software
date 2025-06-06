package com.example.HMS_UI.dto;

import com.example.HMS_UI.constant.CONTACT_STATUS;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ContactRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileno;

    @NotBlank(message = "Message is required")
    private String message;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;
}
