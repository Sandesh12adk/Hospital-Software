package com.example.HMS_UI.dto;

import lombok.Data;

@Data
public class PatientDashboardDTO {
    private int patientId;
    private String name;
    private String email;
    private int age;
    private String gender;  // or could be an Enum
    private int appointmentCount;
}
