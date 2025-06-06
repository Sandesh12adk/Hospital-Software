// DoctorService.java
package com.example.HMS_UI.service;

import com.example.HMS_UI.model.Department;
import com.example.HMS_UI.model.Doctor;
import com.example.HMS_UI.repo.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepo doctorRepo;

    @Autowired
    public DoctorService(DoctorRepo doctorRepository) {
        this.doctorRepo = doctorRepository;
    }

    public List<Doctor> getDoctorsByDepartment(int departmentId) {
        Department department = new Department();
        department.setId(departmentId);
        return doctorRepo.findByDepartment(department);
    }

    public Doctor getDoctorById(int id) {
        return doctorRepo.findById(id).orElse(null);
    }
    public void deleteById(int id){
        System.out.println(id);
        doctorRepo.deleteById(id);
    }
}