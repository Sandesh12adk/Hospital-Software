package com.example.HMS_UI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO for saving department details")
public class DepartmentSaveDTO {

    @NotBlank(message = "Depapatent Name cannot be Blank")
    @Schema(description = "Name of the department", example = "Cardiology")
    private String name;

    @NotBlank(message = "Department Descriptoin is needed")
    @Schema(description = "Description of the department", example = "Handles all heart-related treatments")
    private String description;
}
