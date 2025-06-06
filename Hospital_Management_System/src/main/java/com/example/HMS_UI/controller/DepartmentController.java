package com.example.HMS_UI.controller;

import com.example.HMS_UI.dto.DepartmentDTO;
import com.example.HMS_UI.dto.DepartmentSaveDTO;
import com.example.HMS_UI.exception.ResourceNotFoundException;
import com.example.HMS_UI.model.Department;
import com.example.HMS_UI.service.DepartmentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/department","/departments"})
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

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute DepartmentSaveDTO departmentSaveDTO,
                       BindingResult result,
                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin_dashboard"; // return to form with errors
        }

        Department department = new Department();
        department.setName(departmentSaveDTO.getName());
        department.setDescription(departmentSaveDTO.getDescription());
        departmentService.save(department);

        redirectAttributes.addFlashAttribute("successMessage", "Department added successfully!");
        return "redirect:/admin/dashboard";
    }

    private DepartmentDTO createDepartmentDTO(Department department){
        DepartmentDTO departmentDTO= new DepartmentDTO();
        departmentDTO.setDepartmentId(department.getId());
        departmentDTO.setName(department.getName());
        departmentDTO.setDescription(department.getDescription());
        departmentDTO.setNoOfDoctors(departmentService.noOfDoctors(department));

        return departmentDTO;
    }
    @GetMapping
    public String listDepartments(Model model,
                                  @RequestParam(required = false) String searchQuery) {
        List<DepartmentDTO> departments;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            departments = StreamSupport
                    .stream(departmentService.findAll().spliterator(), false)
                    .collect(Collectors.toList())
                    .stream()
                    .map(department -> {return createDepartmentDTO(department);})
                    .toList();
        } else {
            departments = StreamSupport
                    .stream(departmentService.findAll().spliterator(), false)
                    .collect(Collectors.toList())
                    .stream()
                    .map(department -> {return createDepartmentDTO(department);})
                    .toList();
        }

        model.addAttribute("departments", departments);
        model.addAttribute("searchQuery", searchQuery);
        return "departments"; // Template name should match your HTML file
    }
}
