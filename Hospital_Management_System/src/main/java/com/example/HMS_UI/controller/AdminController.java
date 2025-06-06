package com.example.HMS_UI.controller;

import com.example.HMS_UI.constant.CONTACT_STATUS;
import com.example.HMS_UI.constant.USER_ROLE;
import com.example.HMS_UI.dto.AdminSaveDTO;
import com.example.HMS_UI.dto.AppointmentSaveDTO;
import com.example.HMS_UI.dto.DepartmentSaveDTO;
import com.example.HMS_UI.dto.DoctorSaveDTO;
import com.example.HMS_UI.model.User;
import com.example.HMS_UI.service.AppointmentService;
import com.example.HMS_UI.service.ContactService;
import com.example.HMS_UI.service.DepartmentService;
import com.example.HMS_UI.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@Controller
@RequestMapping("/admin")
@CrossOrigin(origins = "*") // Only for local development!
@Tag(name="Admin APIs")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ContactService contactService;
    //Admin only
    @Operation(
            summary = "Register new admin",
            description = "Accessible by ADMIN only",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid ID or parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Not Found - No resource found with given ID"),
            @ApiResponse(responseCode = "409", description = "Conflict - Resource already exists or violates constraints"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    }
    )
    @PostMapping("/register")
    public ResponseEntity<String> saveAdmin(@Valid @RequestBody AdminSaveDTO adminSaveDTO) {
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(5);
        User user= new User();
        user.setRole(USER_ROLE.ADMIN);
        user.setPassword(encoder.encode(adminSaveDTO.getPassword()));
        user.setEmail(adminSaveDTO.getEmail());
        user.setName(adminSaveDTO.getName());
        userService.save(user);
        return ResponseEntity.ok("Saved");
    }
    @GetMapping("/dashboard")
    public String adminDashboard(Model model){
        Pageable pageable=null;
        CONTACT_STATUS status=null;
        String search=null;
        int unreadCount=contactService.getAllContacts(pageable,status,search).getSize();
        model.addAttribute("unreadCount",unreadCount);
        model.addAttribute("appointmentCount",appointmentService.appointmentCount());
        model.addAttribute("doctorCount",userService.doctorCount(USER_ROLE.DOCTOR));
        model.addAttribute("patientCount",userService.patientCount(USER_ROLE.PATIENT));
        model.addAttribute("departmentCount",departmentService.departmentCount());
        model.addAttribute("departments",departmentService.departmentDashboardDTO());
        model.addAttribute("patients",userService.patientDashboardDTOList());
        model.addAttribute("doctors",userService.DoctorDashboardDTOList());
        model.addAttribute("recentAppointments",appointmentService.dashboardAppointmentDTOList());
        model.addAttribute("appointments",appointmentService.dashboardAppointmentDTOList());
        model.addAttribute("appointmentSaveDTO", new AppointmentSaveDTO());
        model.addAttribute("doctorSaveDTO", new DoctorSaveDTO());
        model.addAttribute("departmentSaveDTO", new DepartmentSaveDTO());
        return "admin_dashboard";
    }
}