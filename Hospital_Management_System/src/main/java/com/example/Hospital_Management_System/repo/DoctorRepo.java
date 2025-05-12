package com.example.Hospital_Management_System.repo;

import com.example.Hospital_Management_System.model.Doctor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DoctorRepo extends CrudRepository<Doctor, Integer> {
}
