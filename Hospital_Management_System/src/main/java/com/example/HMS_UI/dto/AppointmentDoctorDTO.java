package com.example.HMS_UI.dto;

import com.example.HMS_UI.constant.APPOINTMENT_STATUS;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDoctorDTO {
    private int appointmentId;
    private LocalDate localDate;
    private LocalTime localTime;
    private String reason;
    private String appointmentStatus;
    private int patientId;
    private String patientName;
}
