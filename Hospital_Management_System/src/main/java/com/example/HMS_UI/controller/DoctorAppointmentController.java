package com.example.HMS_UI.controller;

import com.example.HMS_UI.constant.APPOINTMENT_STATUS;
import com.example.HMS_UI.dto.AppointmentDoctorDTO;
import com.example.HMS_UI.model.Doctor;
import com.example.HMS_UI.service.AppointmentService;
import com.example.HMS_UI.service.DoctorService;
import com.example.HMS_UI.service.GrantAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/doctor/appointments")
public class DoctorAppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    @Autowired
    public DoctorAppointmentController(AppointmentService appointmentService,
                                       DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    @GetMapping
    public String getFilteredAppointments(
            @RequestParam(required = false) APPOINTMENT_STATUS status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Model model) {

        // Get current doctor
        Doctor doctor =new GrantAccess().getAuthenticationUser().getDoctor();


        // Get filtered appointments
        List<AppointmentDoctorDTO> appointments = appointmentService.findFilteredAppointmentsForDoctor(
                doctor.getId(),
                status,
                fromDate,
                toDate
        );

        model.addAttribute("appointments", appointments);
        model.addAttribute("statusParam", status);
        model.addAttribute("fromDateParam", fromDate);
        model.addAttribute("toDateParam", toDate);

        return "doctor/filtered-appointments";
    }

    @PostMapping("/{id}/schelduded")
    public String completeAppointment(@PathVariable int id) {
        appointmentService.updateAppointmentStatusToSchelduded(id);
        return "redirect:/doctor/appointments";
    }

    @PostMapping("/{id}/cancel")
    public String cancelAppointment(@PathVariable int id) {
        appointmentService.updateAppointmentStatusToCancled(id);
        return "redirect:/doctor/appointments";
    }
}
