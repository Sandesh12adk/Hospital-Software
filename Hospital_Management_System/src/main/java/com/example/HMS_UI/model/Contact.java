package com.example.HMS_UI.model;

import com.example.HMS_UI.constant.CONTACT_STATUS;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contactId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String mobileno;
    @Column(nullable = false)
    private  String message;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CONTACT_STATUS  status;
    @CreatedDate
    private LocalDateTime contactDate;
    //Contact-User Relation
    @ManyToOne
    @JoinColumn(name = "UserId")
    private User user;
}
