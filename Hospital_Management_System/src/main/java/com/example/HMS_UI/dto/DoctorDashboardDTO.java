package com.example.HMS_UI.dto;

import lombok.Data;

@Data
public class DoctorDashboardDTO {
    private int doctorId;
    private String name;
    private String email;
    private String specialization;
    private int departmentId;       // Optional, if needed for backend
    private String departmentName;   // Required for display
    private int appointmentCount;
}
