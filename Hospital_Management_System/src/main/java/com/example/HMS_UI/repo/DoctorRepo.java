package com.example.HMS_UI.repo;

import com.example.HMS_UI.model.Doctor;
import org.springframework.data.repository.CrudRepository;

public interface DoctorRepo extends CrudRepository<Doctor, Integer> {
}
