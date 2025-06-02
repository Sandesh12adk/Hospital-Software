package com.example.Hospital_Management_System.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class emailUpdateDTO {
    @Min(value = 0,message = "Invalid User id")
    private int userId;

    @Email(message = "Invalid email formate")
    private String newEmailId;
}
