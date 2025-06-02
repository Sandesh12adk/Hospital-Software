package com.example.Hospital_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
public class MedicalRecordDTO {
    private String diagnosis;
    private LocalDate RecordCreatedOn;
    private String medicineName;
    private String note;
    private int appointmentId;
    private String doctorName;


}
