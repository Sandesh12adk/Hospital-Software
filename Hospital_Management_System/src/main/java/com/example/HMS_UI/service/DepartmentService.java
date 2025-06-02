package com.example.HMS_UI.service;

import com.example.HMS_UI.repo.DepartmentRepo;
import com.example.HMS_UI.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepo departmentRepo;

    public Department save(Department department){return departmentRepo.save(department);}

    public Iterable<Department> findAll() {return departmentRepo.findAll();}

    public Optional<Department> findById(int id) {
        return departmentRepo.findById(id);
    }

    public int noOfDoctors(Department department){
        return department.getDoctors()!=null ? department.getDoctors().size(): 0;
    }
}
