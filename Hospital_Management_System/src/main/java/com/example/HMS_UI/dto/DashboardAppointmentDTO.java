package com.example.HMS_UI.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DashboardAppointmentDTO {
    private int appointmentId;
    private int patientId;
    private String patientName;
    private int doctorId;
    private String doctorName;
    private String departmentName;
    private LocalDate localDate;
    private LocalTime localTime;
    private String reason;
    private String appointmentStatus;
}
