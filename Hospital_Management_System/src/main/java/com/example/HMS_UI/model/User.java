package com.example.HMS_UI.model;

import com.example.HMS_UI.constant.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private USER_ROLE role;

    private String password;

    //User Doctor relation
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Doctor doctor;

    //User Patient Relation
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Patient patient;

}
