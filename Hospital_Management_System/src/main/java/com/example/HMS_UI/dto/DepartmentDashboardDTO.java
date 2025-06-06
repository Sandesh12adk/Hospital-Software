package com.example.HMS_UI.dto;

import com.example.HMS_UI.model.Doctor;
import lombok.Data;

@Data
public class DepartmentDashboardDTO {
    private int departmentId;      // Unique identifier
    private String name;            // e.g., "Cardiology", "Neurology"
    private String description;     // e.g., "Heart-related treatments"
    private Integer noOfDoctors;
}
