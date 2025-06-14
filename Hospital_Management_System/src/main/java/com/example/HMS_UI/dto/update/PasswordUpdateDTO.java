package com.example.HMS_UI.dto.update;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordUpdateDTO {

  //  @JsonIgnore
    private String oldPassword;

    @Size(min = 8,message = "Password size must be atlead 8")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
    @Pattern(regexp = ".*[!@#$%^&*()].*", message = "Password must contain at least one special character")
    private String newPassword;

}
