package com.example.Hospital_Management_System.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
    public class AppointmentSaveDTO {

        @NotNull(message = "Please fix the appointment date")
        @Schema(example = "2025-05-10",description = "yy-mm-dd")
        private LocalDate date;

        @NotNull(message = "Please fix the appointment time")
        @Schema(example = "05:40", description = "HH:MM")
        private LocalTime time;

        @NotBlank(message = "Reason is required")
        private String reason;

        @Schema(description = "Id of the doctor")
        private int doctorId;

        @Schema(description = "Id of the Patient")
        private int patientId;
    }
