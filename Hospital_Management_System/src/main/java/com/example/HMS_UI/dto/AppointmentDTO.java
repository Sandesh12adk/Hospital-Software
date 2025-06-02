package com.example.Hospital_Management_System.dto;

import com.example.Hospital_Management_System.constant.APPOINTMENT_STATUS;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private int appointmentId;
    private LocalDate localDate;
    private LocalTime localTime;
    private String reason;
    private APPOINTMENT_STATUS appointmentStatus;
    private int doctorId;
    private int patientId;
}
