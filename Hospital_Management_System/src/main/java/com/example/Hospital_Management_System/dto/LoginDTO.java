package com.example.Hospital_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LoginDTO {
    private String jwtToken;
    protected String userName;
    protected List<String> roles;
}
