// PatientService.java
package com.example.HMS_UI.service;
import com.example.HMS_UI.model.Appointment;
import com.example.HMS_UI.model.Patient;
import com.example.HMS_UI.model.User;
import com.example.HMS_UI.repo.AppointmentRepo;
import com.example.HMS_UI.repo.PatientRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientService {

    private final PatientRepo patientRepo;
    private final GrantAccess grantAccess;
    private final AppointmentRepo appointmentRepo;
    @Autowired
    public PatientService(PatientRepo patientRepo,GrantAccess grantAccess,AppointmentRepo appointmentRepo) {
        this.patientRepo = patientRepo;
        this.grantAccess= grantAccess;
        this.appointmentRepo= appointmentRepo;
    }


    public Optional<Patient> getCurrentPatient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        User user = new GrantAccess().getAuthenticationUser();
        return patientRepo.findByUserIdWithUser(user.getId());
    }

}