package com.example.Hospital_Management_System.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepartmentSaveDTO {
    @NotBlank(message = "Depapatent Name cannot be Blank")
    private String name;
    @NotBlank(message = "Department Descriptoin is needed")
    private String description;
}
