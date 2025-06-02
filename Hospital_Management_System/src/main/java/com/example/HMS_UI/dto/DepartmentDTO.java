package com.example.Hospital_Management_System.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentDTO {

    private int departmentId;
    private String name;
    private String description;
    private int noOfDoctors;
}
