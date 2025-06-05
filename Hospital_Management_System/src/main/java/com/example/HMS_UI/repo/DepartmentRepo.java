package com.example.HMS_UI.repo;

import com.example.HMS_UI.model.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepo extends CrudRepository<Department, Integer> {
    List<Department> findAllByOrderByNameAsc();
}
