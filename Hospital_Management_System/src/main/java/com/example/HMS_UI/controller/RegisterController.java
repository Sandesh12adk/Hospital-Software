package com.example.HMS_UI.controller;

import com.example.HMS_UI.constant.USER_ROLE;
import com.example.HMS_UI.dto.DoctorSaveDTO;
import com.example.HMS_UI.dto.PatientDTO;
import com.example.HMS_UI.dto.PatientSaveDTO;
import com.example.HMS_UI.exception.ResourceNotFoundException;
import com.example.HMS_UI.model.Department;
import com.example.HMS_UI.model.Doctor;
import com.example.HMS_UI.model.Patient;
import com.example.HMS_UI.model.User;
import com.example.HMS_UI.repo.DoctorRepo;
import com.example.HMS_UI.repo.PatientRepo;
import com.example.HMS_UI.service.AppointmentService;
import com.example.HMS_UI.service.DepartmentService;
import com.example.HMS_UI.service.GrantAccess;
import com.example.HMS_UI.service.UserService;
import com.example.HMS_UI.service.securityservice.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller

public class RegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private GrantAccess grantAccess;
    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private DoctorRepo doctorRepo;
    //permit all
    //final

    @GetMapping("/register_patient")
    public String showForm(Model model) {
        model.addAttribute("patientSaveDTO", new PatientSaveDTO());
        return "p_r_p";
    }
    @GetMapping("/regi")
    public String showDoctorForm(Model model) {
      model.addAttribute("doctorSaveDTO", new DoctorSaveDTO());
        List<Department> departments = StreamSupport
               .stream(departmentService.findAll().spliterator(), false)
                .collect(Collectors.toList());
       departments.forEach(a->
               System.out.println(a.getName()));

       model.addAttribute("departments", departments);
        return "doctor_registration"; // Thymeleaf template name
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/save")
    public String saveDoctor(@ModelAttribute("doctorSaveDTO") @Valid DoctorSaveDTO doctorSaveDTO,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            // Repopulate departments if there are errors
            List<Department> departments = StreamSupport
                    .stream(departmentService.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            model.addAttribute("departments", departments);
            return "doctor_registration";

        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);
        Department department = departmentService.findById(doctorSaveDTO.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department Not Found"));

        User user = new User();
        user.setName(doctorSaveDTO.getName());
        user.setPassword(encoder.encode(doctorSaveDTO.getPassword()));
        user.setRole(USER_ROLE.DOCTOR);
        user.setEmail(doctorSaveDTO.getEmail());
        Doctor doctor = new Doctor();
        doctor.setDepartment(department);
        doctor.setSpecialization(doctorSaveDTO.getSpecialization());
        doctor.setUser(user);

        user.setDoctor(doctor);
        userService.save(user);

        return "redirect:/login?registered";
    }
    @PreAuthorize("permitAll()")
    @PostMapping("save_by_admin")
    public String saveDoctorByAmind(@ModelAttribute("doctorSaveDTO") @Valid DoctorSaveDTO doctorSaveDTO,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            // Repopulate departments if there are errors
            List<Department> departments = StreamSupport
                    .stream(departmentService.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            model.addAttribute("departments", departments);
            return "doctor_registration";

        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);
        Department department = departmentService.findById(doctorSaveDTO.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department Not Found"));

        User user = new User();
        user.setName(doctorSaveDTO.getName());
        user.setPassword(encoder.encode(doctorSaveDTO.getPassword()));
        user.setRole(USER_ROLE.DOCTOR);
        user.setEmail(doctorSaveDTO.getEmail());
        Doctor doctor = new Doctor();
        doctor.setDepartment(department);
        doctor.setSpecialization(doctorSaveDTO.getSpecialization());
        doctor.setUser(user);

        user.setDoctor(doctor);
        userService.save(user);

        return "redirect:/admin/dashboard#doctor";
    }

    }
}
