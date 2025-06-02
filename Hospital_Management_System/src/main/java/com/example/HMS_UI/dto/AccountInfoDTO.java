package com.example.Hospital_Management_System.dto;

import com.example.Hospital_Management_System.service.GrantAccess;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@NoArgsConstructor
public class AccountInfoDTO {
    private int roleId;
    protected String userName;
    protected List<String> roles;
    protected int userId;
}
