package com.example.HMS_UI.service;

import com.example.HMS_UI.dto.DepartmentDashboardDTO;
import com.example.HMS_UI.repo.DepartmentRepo;
import com.example.HMS_UI.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

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
    public int departmentCount() {
        return (int) StreamSupport.stream(departmentRepo.findAll().spliterator(), false)
                .count();
    }
    public List<DepartmentDashboardDTO> departmentDashboardDTO(){
        List<DepartmentDashboardDTO> departmentDashboardDTOList= new ArrayList<>();
        departmentRepo.findAll().forEach(department -> {
            DepartmentDashboardDTO departmentDashboardDTO= new DepartmentDashboardDTO();
            departmentDashboardDTO.setDepartmentId(department.getId());
            departmentDashboardDTO.setName(department.getName());
            departmentDashboardDTO.setNoOfDoctors(department.getDoctors().size());
            departmentDashboardDTO.setDescription(department.getDescription());
            departmentDashboardDTOList.add(departmentDashboardDTO);
        });
        return  departmentDashboardDTOList;
    }

}
