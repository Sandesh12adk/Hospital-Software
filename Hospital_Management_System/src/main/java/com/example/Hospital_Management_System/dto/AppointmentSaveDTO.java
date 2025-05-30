package com.example.Hospital_Management_System.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(description = "DTO for scheduling an appointment between a doctor and a patient")
public class AppointmentSaveDTO {

    @NotNull(message = "Please fix the appointment date")
    @Schema(
            description = "Date of the appointment in yyyy-MM-dd format",
            example = "2025-05-10"
    )
    private LocalDate date;

    @NotNull(message = "Please fix the appointment time")
    @Schema(
            description = "Time of the appointment in HH:mm format (24-hour)",
            example = "14:30"
    )
    private LocalTime time;

    @NotBlank(message = "Reason is required")
    @Schema(
            description = "Short description of the reason for the appointment",
            example = "Regular health check-up"
    )
    private String reason;

    @Schema(
            description = "ID of the doctor with whom the appointment is scheduled",
            example = "12"
    )
    private int doctorId;

    @Schema(
            description = "ID of the patient who is making the appointment",
            example = "34"
    )
    private int patientId;
}
