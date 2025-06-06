package com.example.HMS_UI.controller;

import com.example.HMS_UI.constant.APPOINTMENT_STATUS;
import com.example.HMS_UI.dto.AppointmentDTOA;
import com.example.HMS_UI.mapper.AppointmentMapper;
import com.example.HMS_UI.model.Appointment;
import com.example.HMS_UI.model.Patient;
import com.example.HMS_UI.service.AppointmentService;
import com.example.HMS_UI.service.DepartmentService;
import com.example.HMS_UI.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/patient/appointments")
public class PatientAppointmentController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DepartmentService departmentService;
    private final AppointmentMapper appointmentMapper;

    @Autowired
    public PatientAppointmentController(AppointmentService appointmentService,
                                        PatientService patientService,
                                        DepartmentService departmentService,
                                        AppointmentMapper appointmentMapper) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.departmentService = departmentService;
        this.appointmentMapper = appointmentMapper;
    }

    @GetMapping
    public String getAppointments(@RequestParam(required = false) String status,
                                  @RequestParam(required = false) String fromDate,
                                  @RequestParam(required = false) String toDate,
                                  Model model) {
        Optional<Patient> patientOpt = patientService.getCurrentPatient();
        if (patientOpt.isEmpty()) {
            return "redirect:/login";
        }

        Patient patient = patientOpt.get();

        // Set default filters
        if (status == null || status.isEmpty()) {
            status = APPOINTMENT_STATUS.PENDING.name();
        }

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate from = null;
        LocalDate to = null;

        try {
            if (fromDate != null && !fromDate.isEmpty()) {
                from = LocalDate.parse(fromDate, formatter);
            } else {
                from = today;
                fromDate = from.format(formatter); // for showing in UI
            }

            if (toDate != null && !toDate.isEmpty()) {
                to = LocalDate.parse(toDate, formatter);
            }
        } catch (DateTimeParseException e) {
            // Handle invalid date format if necessary
        }

        List<Appointment> filteredAppointments = appointmentService.getAppointmentsByPatientWithFilters(
                patient.getId(),
                status,
                from,
                to
        );

        List<AppointmentDTOA> upcomingAppointments = filteredAppointments.stream()
                .filter(a -> !a.getDate().isBefore(today)) // today or future
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());

        List<AppointmentDTOA> pastAppointments = filteredAppointments.stream()
                .filter(a -> a.getDate().isBefore(today))
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());

        model.addAttribute("patient", patient);
        model.addAttribute("user", patient.getUser());
        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("pastAppointments", pastAppointments);
        model.addAttribute("departments", departmentService.getAllDepartments());

        // Preserve filter values in Thymeleaf
        model.addAttribute("statusParam", status);
        model.addAttribute("fromDateParam", fromDate);
        model.addAttribute("toDateParam", toDate);

        return "patient/appointments";
    }

}
