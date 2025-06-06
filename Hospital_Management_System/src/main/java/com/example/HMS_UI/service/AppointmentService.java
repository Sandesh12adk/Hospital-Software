package com.example.HMS_UI.service;

import com.example.HMS_UI.constant.APPOINTMENT_STATUS;
import com.example.HMS_UI.dto.DashboardAppointmentDTO;
import com.example.HMS_UI.exception.ResourceNotFoundException;
import com.example.HMS_UI.model.Appointment;
import com.example.HMS_UI.model.Patient;
import com.example.HMS_UI.repo.AppointmentRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {
    private final AppointmentRepo appointmentRepo;
    @Autowired
    public AppointmentService(AppointmentRepo appointmentRepo){this.appointmentRepo= appointmentRepo;}
    public Appointment save(Appointment appointment) {return appointmentRepo.save(appointment);}
    public List<Appointment> findByDocId(int id){return appointmentRepo.findBYDoctorId(id);}
    public List<Appointment> findByPatientId(int id){ return appointmentRepo.findBYPatientId(id);}
    public List<Appointment> findAll(){ return appointmentRepo.findAll();}
    public Optional<Appointment> findById(int id){return appointmentRepo.findById(id); }

    public void updateAppointmentStatusToCompleted(int appointmentId){
        Appointment appointment= appointmentRepo.findById(appointmentId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Appointment doesnot exist with id "+ appointmentId
        ));
        appointment.setStatus(APPOINTMENT_STATUS.COMPLETED);
        appointment.setId(appointmentId); // Not necessary because when we fetch the appointment the id is
       // already assigned
        save(appointment);
    }
    public void updateAppointmentStatusToSchelduded(int appointmentId){
        Appointment appointment= appointmentRepo.findById(appointmentId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Appointment doesnot exist with id "+ appointmentId
                        ));
        appointment.setStatus(APPOINTMENT_STATUS.SCHEDULDED);
        save(appointment);
    }

    public void updateAppointmentStatusToCancled(int appointmentId) {
        Appointment appointment= appointmentRepo.findById(appointmentId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Appointment doesnot exist with id "+ appointmentId
                        ));
        appointment.setStatus(APPOINTMENT_STATUS.CANCLED);
        save(appointment);
    }
    @Scheduled(cron = "0 0  0 * * *")//Check at midnight every day
    public void cancelAppointmentAfter10Days(){
        List<Appointment> appointmentList= findAll()
                .stream().filter((appointment)-> {
                           return appointment.getStatus() == APPOINTMENT_STATUS.PENDING ||
                                    appointment.getStatus() == APPOINTMENT_STATUS.SCHEDULDED;
                        } ).collect(Collectors.toList());
        for(Appointment appointment: appointmentList){
            long days= ChronoUnit.DAYS.between(appointment.getDate(), LocalDate.now());
            if(days>10){
                updateAppointmentStatusToCancled(appointment.getId());
            }
        }
    }
    public int appointmentCount(){
        return appointmentRepo.findAll().size();
    }
    public List<DashboardAppointmentDTO> dashboardAppointmentDTOList(){
       return appointmentRepo.findAll()
                .stream()
                .filter(appointment -> {return appointment.getDoctor()!=null;})
                .filter(appointment -> {return appointment.getPatient()!=null;})
                .filter(appointment -> {return appointment.getDate()!=null;})
                .filter(appointment -> {return appointment.getTime()!=null;})
               .filter(appointment -> {return appointment.getPatient().getUser()!=null;})
               .filter(appointment -> {return appointment.getDoctor().getUser()!=null;})
                .map(appointment -> {
                    DashboardAppointmentDTO dashboardAppointmentDTO= new DashboardAppointmentDTO();
                    dashboardAppointmentDTO.setAppointmentId(appointment.getId());
                    dashboardAppointmentDTO.setDepartmentName(appointment.getDoctor().getDepartment().getName());
                    dashboardAppointmentDTO.setAppointmentStatus(appointment.getStatus().name());
                    dashboardAppointmentDTO.setReason(appointment.getReason());
                    dashboardAppointmentDTO.setDoctorName(appointment.getDoctor().getUser().getName());
                    dashboardAppointmentDTO.setLocalDate(appointment.getDate());
                    dashboardAppointmentDTO.setLocalTime(appointment.getTime());
                    dashboardAppointmentDTO.setPatientId(appointment.getPatient().getId());
                    dashboardAppointmentDTO.setDoctorId(appointment.getDoctor().getId());
                    dashboardAppointmentDTO.setPatientName(appointment.getPatient().getUser().getName());
                    return dashboardAppointmentDTO;
                }).toList();

    }
    public List<Appointment> getUpcomingAppointments(int patientId) {
        return appointmentRepo.findUpcomingAppointments(patientId, LocalDate.now());
    }

    public List<Appointment> getPastAppointments(int patientId) {
        return appointmentRepo.findPastAppointments(patientId, LocalDate.now());
    }

    public Optional<Appointment> getAppointmentById(int id) {
        return appointmentRepo.findById(id);
    }

    public void cancelAppointment(int id, String reason) {
        appointmentRepo.findById(id).ifPresent(appointment -> {
            appointment.setStatus(APPOINTMENT_STATUS.CANCELLED);
            appointment.setReason(reason);
            appointmentRepo.save(appointment);
        });
    }
    public List<Appointment> getAppointmentsByPatientWithFilters(int patientId,
                                                                 String status,
                                                                 LocalDate fromDate,
                                                                 LocalDate toDate) {
        String statusFilter = StringUtils.hasText(status) ? status : null;
        APPOINTMENT_STATUS appointmentStatus= APPOINTMENT_STATUS.valueOf(statusFilter.trim().toUpperCase());
        return appointmentRepo.findFilteredAppointments(
                patientId,
                appointmentStatus,
                fromDate,
                toDate
        );
    }
    public List<Appointment> findByPatientAndDateGreaterThanEqual(Patient patient, LocalDate date){
        return appointmentRepo.findByPatientAndDateGreaterThanEqual(patient,date);
    }
}
