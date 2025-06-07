package com.example.HMS_UI.model;

import com.example.HMS_UI.constant.APPOINTMENT_STATUS;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointment")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Appointment {
  public Appointment(){
    this.status= APPOINTMENT_STATUS.PENDING;
  }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)   //programatically
    private APPOINTMENT_STATUS status;

    @Column(nullable = false)
    private String reason;


    @Column(nullable = false)
    @CreatedDate
    private LocalDate date;



    @Column(nullable = false, columnDefinition = "TIME")
    private LocalTime time;

    //Appointment Doctor Relation
    @ManyToOne
    @JoinColumn(name = "Doctor_Id")
    private Doctor doctor;

    //Appointment Patient Relation
    @ManyToOne
    @JoinColumn(name = "Patient_Id",nullable = false)
    private Patient patient;
    private String paymentStatus= "Unpaid";
    //Appointment MedicalRecord Relation
   @OneToOne(mappedBy = "appointment")
  private MedicalRecord medicalRecord;
}
