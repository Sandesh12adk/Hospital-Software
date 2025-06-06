package com.example.HMS_UI.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookAppointmentDTO {
    @NotNull(message = "Doctor is required")
    private Integer doctorId;

    @NotBlank(message = "Reason is required")
    private String reason;

    @Future(message = "Appointment date must be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotNull(message = "Time is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;
}