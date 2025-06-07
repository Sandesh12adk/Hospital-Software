package com.example.HMS_UI.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String method;
    private double amount;
    private String status;
    private LocalDateTime paymentDate;
    @OneToOne
    private Appointment appointment;
}

