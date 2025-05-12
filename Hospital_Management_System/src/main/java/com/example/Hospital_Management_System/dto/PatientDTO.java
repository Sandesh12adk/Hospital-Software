package com.example.Hospital_Management_System.dto;

import lombok.Data;

@Data
public class PatientDTO {
    private String name;
    private String email;
    private String role;
    private String gender;
    private int age;
    private int userId;
    private int patientId;
}
