package com.example.Hospital_Management_System.controller;

import com.example.Hospital_Management_System.dto.DepartmentDTO;
import com.example.Hospital_Management_System.dto.DepartmentSaveDTO;
import com.example.Hospital_Management_System.dto.DoctorSaveDTO;
import com.example.Hospital_Management_System.exception.ResourceNotFoundException;
import com.example.Hospital_Management_System.model.Department;
import com.example.Hospital_Management_System.model.User;
import com.example.Hospital_Management_System.service.DepartmentService;
import com.example.Hospital_Management_System.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/department")
@Slf4j
@CrossOrigin(origins="*")
@Tag(
        name = "Department APIs",
        description = "Endpoints for managing hospital departments. " +
                "Includes creation (ADMIN only), retrieval of all departments," +
                " and lookup by ID (public access)."
)

public class DepartmentController {

        @Autowired
        private DepartmentService departmentService;

        //Admin only
        @Operation(
                summary = "Create a new department",
                description = "Allows an ADMIN to create a new medical department with a name and description.",
                security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    )
        @PostMapping("/save")
        public ResponseEntity<DepartmentDTO> save(@Valid @RequestBody DepartmentSaveDTO departmentSaveDTO){
           Department department= new Department();
           department.setName(departmentSaveDTO.getName());
           department.setDescription(departmentSaveDTO.getDescription());
           departmentService.save(department);
            return ResponseEntity.ok(createDepartmentDTO(department));
        }

    /**
     * PUBLIC access
     */
    @Operation(
            summary = "Get all departments",
            description = "Retrieves a list of all medical departments. This endpoint is publicly accessible."
    )
        @GetMapping("/findall")
        public ResponseEntity<List<DepartmentDTO>> findAll(){
            List<DepartmentDTO> departmentDTOList= new ArrayList<>();
          departmentService.findAll().forEach((department)->{
              departmentDTOList.add(createDepartmentDTO(department));
                  }
          );
          return ResponseEntity.ok(departmentDTOList);
        }

    /**
     * PUBLIC access
     */
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Get department by ID",
            description = "Fetches a department's details using its ID. This endpoint is publicly accessible."
    )
        @GetMapping("/{id}")
        public ResponseEntity<DepartmentDTO> findById(@PathVariable int id){
            Department department= departmentService.findById(id).orElseThrow(()->
                    new ResourceNotFoundException("Department Not Found"));
            return ResponseEntity.ok(createDepartmentDTO(department));
        }

    private DepartmentDTO createDepartmentDTO(Department department){
        DepartmentDTO departmentDTO= new DepartmentDTO();
        departmentDTO.setDepartmentId(department.getId());
        departmentDTO.setName(department.getName());
        departmentDTO.setDescription(department.getDescription());
        departmentDTO.setNoOfDoctors(departmentService.noOfDoctors(department));

        return departmentDTO;
    }

}
