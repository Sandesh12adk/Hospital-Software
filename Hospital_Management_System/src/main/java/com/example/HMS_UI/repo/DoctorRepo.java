
// DoctorRepository.java
package com.example.HMS_UI.repo;

import com.example.HMS_UI.model.Department;
import com.example.HMS_UI.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Integer> {
    List<Doctor> findByDepartment(Department department);
}