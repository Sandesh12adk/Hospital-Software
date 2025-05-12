package com.example.Hospital_Management_System.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MedicalRecordSaveDTO {

    @Column(nullable = false)
    @NotBlank(message = "Diagnosis cannot be Blank")
    private String diagnosis;

    @NotBlank(message = "Diagnosis cannot be Blank")
    private String prescription;

    @NotBlank(message = "Noter cannot be Blank")
    private String note;

    @Min(value = 1, message = "Please Specify the Appointment Id")
    private int appointmentId;
}
