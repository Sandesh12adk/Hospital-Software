package com.example.HMS_UI.dto;

import com.example.HMS_UI.constant.CONTACT_STATUS;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContactDTO {
    private int contactId;
    private String name;
    private String mobileno;
    private String message;
    private String subject;
    private String email;
    private CONTACT_STATUS status;
    private LocalDateTime contactDate;
    private int userId;
    private String username;
}