package com.example.Hospital_Management_System.model;

import com.example.Hospital_Management_System.constant.APPOINTMENT_STATUS;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

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


    @Column(nullable = false)
    private LocalTime time;

    //Appointment Doctor Relation
    @ManyToOne
    @JoinColumn(name = "Doctor_Id")
    private Doctor doctor;

    //Appointment Patient Relation
    @ManyToOne
    @JoinColumn(name = "Patient_Id",nullable = false)
    private Patient patient;

    //Appointment MedicalRecord Relation

   @OneToOne(mappedBy = "appointment")
  private MedicalRecord medicalRecord;

}
