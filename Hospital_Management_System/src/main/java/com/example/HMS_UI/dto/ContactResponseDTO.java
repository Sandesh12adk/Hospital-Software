package com.example.HMS_UI.dto;

import com.example.HMS_UI.constant.CONTACT_STATUS;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class ContactResponseDTO {
    private int userId;
    private String Username;
    private String mobileno;
    private  String message;
    private String subject;
    private String email;
    private CONTACT_STATUS status;
}
