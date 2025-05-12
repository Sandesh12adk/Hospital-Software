package com.example.Hospital_Management_System.controller;

import com.example.Hospital_Management_System.constant.APPOINTMENT_STATUS;
import com.example.Hospital_Management_System.constant.USER_ROLE;
import com.example.Hospital_Management_System.dto.AppointmentDTO;
import com.example.Hospital_Management_System.dto.AppointmentSaveDTO;
import com.example.Hospital_Management_System.exception.ResourceNotFoundException;
import com.example.Hospital_Management_System.model.Appointment;
import com.example.Hospital_Management_System.model.Doctor;
import com.example.Hospital_Management_System.model.Patient;
import com.example.Hospital_Management_System.model.User;
import com.example.Hospital_Management_System.repo.DoctorRepo;
import com.example.Hospital_Management_System.repo.PatientRepo;
import com.example.Hospital_Management_System.service.AppointmentService;
import com.example.Hospital_Management_System.service.GrantAccess;
import com.example.Hospital_Management_System.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/appointment")
@CrossOrigin(origins = "*") // Only for local development!
@Tag(name="Appointment APIs",description= "Create,  Find-By-Doc-Id,  Find-By-Patient-Id,  Find-By-Doc-And-Pat-Id,  Find-All")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private GrantAccess grantAccess;

    //access: adimin only
    @PostMapping("/create")
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentSaveDTO appointmentSaveDTO) throws AccessDeniedException {
        Patient patient = patientRepo.findById(appointmentSaveDTO.getPatientId()).orElseThrow(() ->
                new ResourceNotFoundException("Patient Not Found with Id " + appointmentSaveDTO.getPatientId()));
        Doctor doctor = doctorRepo.findById(appointmentSaveDTO.getDoctorId()).orElseThrow(() ->
                new ResourceNotFoundException("Doctor Not Found with Id" + appointmentSaveDTO.getDoctorId()));
        Appointment appointment= new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setReason(appointmentSaveDTO.getReason());
        appointment.setTime(appointmentSaveDTO.getTime());
        appointment.setDate(appointmentSaveDTO.getDate());
        appointmentService.save(appointment);
        return ResponseEntity.ok(createAppointmentDTO(appointment));
    }

    //access: That specific doctor and admin
    @GetMapping("/findbydoctorid/{docId}")
    public ResponseEntity<List<AppointmentDTO>> findBydocId(@PathVariable int docId) throws AccessDeniedException {
        User user = grantAccess.getAuthenticationUser();
        boolean isDoctor = user.getDoctor() != null;
        boolean isAdmin = user.getRole() == USER_ROLE.ADMIN;
        if (isAdmin || (isDoctor && user.getDoctor().getId() == docId)) {

            List<AppointmentDTO> appointmentDTOList = appointmentService.findByDocId(docId)
                    .stream()
                    .map((appointment) ->
                            createAppointmentDTO(appointment)
                    ).collect(toList());
            if (appointmentDTOList.size() == 0) {
                throw new ResourceNotFoundException("No Appointment associated with doctor id " + docId);
            }
            return ResponseEntity.ok(appointmentDTOList);
        }
        throw new AccessDeniedException("Sorry, Access denied");
    }
    //Admin and that specific doctor
   @GetMapping("/findBydocId")
   public ResponseEntity<List<AppointmentDTO>>findBydoctIdByStatus(@RequestParam(required = true) int docId,
                               @RequestParam(required = true) String status)throws  AccessDeniedException{
       // Convert the status string to the enum
       APPOINTMENT_STATUS appointmentStatus;
       try {
           appointmentStatus = APPOINTMENT_STATUS.valueOf(status.toUpperCase()); // convert to enum
       } catch (IllegalArgumentException e) {
           throw new IllegalArgumentException("Invalid status value provided.");
       }
        boolean isAdmin= grantAccess.getAuthenticationUser().getRole()==USER_ROLE.ADMIN;
        boolean isDoctor= grantAccess.getAuthenticationUser().getDoctor()!=null;
        if(isAdmin ||(isDoctor && docId== grantAccess.getAuthenticationUser().getDoctor().getId())) {
            List<AppointmentDTO> appointmentDTOList = appointmentService.findAll()
                    .stream()
                    .filter((appointment) ->
                            (appointment.getDoctor()!=null && appointment.getPatient()!=null) &&
                            ( appointment.getDoctor().getId() == docId &&
                                    appointment.getStatus()==appointmentStatus))
                    .map((appointment) -> {
                        return createAppointmentDTO(appointment);
                    })
                    .toList();
            return ResponseEntity.ok(appointmentDTOList);
        }
        throw new AccessDeniedException("Sorry, Access Denied");
   }


    // access: that specific doctor, that specific patient and admin all
        @GetMapping("/find")
        public ResponseEntity<List<AppointmentDTO>> findByDocAndPatientId ( @RequestParam int doctorId, @RequestParam
        int patientId)throws AccessDeniedException {
            User user = grantAccess.getAuthenticationUser();
            Doctor doctor = null;
            Patient patient = null;
            boolean isDoctor = user.getDoctor() != null;
            boolean isPatient = user.getPatient() != null;
            boolean isAdmin = user.getRole() == USER_ROLE.ADMIN;

            if (isAdmin || (isDoctor && user.getDoctor().getId() == doctorId) ||
                    (isPatient && user.getPatient().getId()== patientId)) {

                List<AppointmentDTO> appointmentDTOList = findBydocId(doctorId).getBody().stream().filter((appointmentDTO) ->
                        appointmentDTO.getPatientId() == patientId
                ).toList();
                if (appointmentDTOList.size() == 0) {
                    throw new ResourceNotFoundException("No Appointment associated with patient id " + patientId);
                }
                return ResponseEntity.ok(appointmentDTOList);
            }
            throw new AccessDeniedException("Sorry, Access denied");
        }
        // access: admin only
        @GetMapping("/findall")
        public ResponseEntity<List<AppointmentDTO>> findAll ()throws AccessDeniedException {
            User user = grantAccess.getAuthenticationUser();
            List<AppointmentDTO> appointmentDTOList = appointmentService.findAll()
                    .stream().map((appointment) -> {
                                return createAppointmentDTO(appointment);
                            }
                    ).toList();
            if (appointmentDTOList.size() == 0) {
                throw new ResourceNotFoundException("Appointments List is Empty");
            }
            return ResponseEntity.ok(appointmentDTOList);
        }

        private AppointmentDTO createAppointmentDTO (Appointment appointment){
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setAppointmentId(appointment.getId());
            if (appointment.getDoctor() != null) {
                appointmentDTO.setDoctorId(appointment.getDoctor().getId());
            }
            if (appointment.getPatient() != null) {

                appointmentDTO.setPatientId(appointment.getPatient().getId());
            }
            appointmentDTO.setLocalDate(appointment.getDate());
            appointmentDTO.setLocalTime(appointment.getTime());
            appointmentDTO.setReason(appointment.getReason());
            appointmentDTO.setAppointmentStatus(appointment.getStatus());
            return appointmentDTO;
        }
    }

