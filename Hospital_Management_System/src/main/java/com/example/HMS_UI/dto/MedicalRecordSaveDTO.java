package com.example.HMS_UI.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO for saving a patient's medical record after an appointment")
public class MedicalRecordSaveDTO {

    @Column(nullable = false)
    @NotBlank(message = "Diagnosis cannot be Blank")
    @Schema(description = "Diagnosis given by the doctor", example = "Type 2 Diabetes")
    private String diagnosis;

    @NotBlank(message = "Diagnosis cannot be Blank")
    @Schema(description = "Prescribed medications or treatment", example = "Metformin 500mg twice daily")
    private String prescription;

    @NotBlank(message = "Noter cannot be Blank")
    @Schema(description = "Doctor's additional note", example = "Patient should reduce sugar intake and follow up in 1 month")
    private String note;

    @Min(value = 1, message = "Please Specify the Appointment Id")
    @Schema(description = "ID of the appointment this record is linked to", example = "45")
    private int appointmentId;
}
