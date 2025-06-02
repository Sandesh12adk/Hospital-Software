package com.example.HMS_UI.controller;

import com.example.HMS_UI.constant.USER_ROLE;
import com.example.HMS_UI.dto.MedicalRecordDTO;
import com.example.HMS_UI.dto.MedicalRecordSaveDTO;
import com.example.HMS_UI.exception.ResourceNotFoundException;
import com.example.HMS_UI.model.Appointment;
import com.example.HMS_UI.model.MedicalRecord;
import com.example.HMS_UI.model.User;
import com.example.HMS_UI.service.AppointmentService;
import com.example.HMS_UI.service.GrantAccess;
import com.example.HMS_UI.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/medicalrecord")
@CrossOrigin(origins = "*")
@Tag(name = "Medical Record APIs", description = "Save Medical Record, Find By Patient Id")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private GrantAccess grantAccess;

    //That specific Doctor only
    @Operation(
            summary = "Save a new medical record",
            description = "Allows the doctor of a specific appointment to save a medical record. Only accessible by the doctor who owns the appointment.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Operation"),
                    @ApiResponse(responseCode = "400", description = "Bad Request - Invalid ID or parameters"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to access this resource"),
                    @ApiResponse(responseCode = "404", description = "Not Found - No resource found with given ID"),
                    @ApiResponse(responseCode = "409", description = "Conflict - Resource already exists or violates constraints"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")}
    )

    @PostMapping("/save")
    public ResponseEntity<MedicalRecordDTO> save(@RequestBody MedicalRecordSaveDTO medicalRecordSaveDTO) {
        User user = grantAccess.getAuthenticationUser();
        Appointment appointment= appointmentService.findById(medicalRecordSaveDTO.getAppointmentId()).orElseThrow(()->
                new ResourceNotFoundException("Cannot find the appoint with provided ID")
        );
        if(appointment.getDoctor().getId()==user.getDoctor().getId()) {
            MedicalRecord medicalRecord= new MedicalRecord();
            medicalRecord.setAppointment(appointment);
            medicalRecord.setNote(medicalRecordSaveDTO.getNote());
            medicalRecord.setDiagnosis(medicalRecordSaveDTO.getDiagnosis());
            medicalRecord.setPrescription(medicalRecordSaveDTO.getPrescription());
            medicalRecordService.save(medicalRecord);
            appointmentService.updateAppointmentStatusToCompleted(appointment.getId());
            MedicalRecordDTO medicalRecordDTO = createMedicalRecordDTO(medicalRecord);
            medicalRecordDTO.setAppointmentId(appointment.getId());
            medicalRecordDTO.setDoctorName(appointmentService.findById(appointment.getId()).get()
                    .getDoctor().getUser().getName());
            return ResponseEntity.ok(medicalRecordDTO);
        }
        throw new ResourceNotFoundException("Please verify the appointment ID and try again");
    }

    //Doctor, that specific patient and admin
    @Operation(
            summary = "Get medical records by patient ID",
            description = "Fetches all medical records associated with a given patient ID. Accessible by admin, all doctors, or the patient themselves.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Operation"),
                    @ApiResponse(responseCode = "400", description = "Bad Request - Invalid ID or parameters"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to access this resource"),
                    @ApiResponse(responseCode = "404", description = "Not Found - No resource found with given ID"),
                    @ApiResponse(responseCode = "409", description = "Conflict - Resource already exists or violates constraints"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")}
    )

    @GetMapping("/find_by_patientid/{patientId}")
    public ResponseEntity<List<MedicalRecordDTO>> findByPatientId(@PathVariable int patientId){
        User user= grantAccess.getAuthenticationUser();
        boolean isAdmin= user.getRole()== USER_ROLE.ADMIN;
        boolean isDoctor= user.getDoctor()!= null;
        boolean isPatient= user.getPatient()!=null;
        if(isAdmin || isDoctor || (isPatient && patientId== user.getPatient().getId())) {
            List<MedicalRecord> medicalRecordList = new ArrayList<>();
            medicalRecordService.findAll().forEach((medicalRecord ->
                    medicalRecordList.add(medicalRecord)
            ));
            List<MedicalRecord> fileteredMedicalRecordList = medicalRecordList.stream().filter((medicalRecord ->
                    medicalRecord.getAppointment() != null &&
                            medicalRecord.getAppointment().getPatient() != null &&
                            medicalRecord.getAppointment().getDoctor() != null &&
                            medicalRecord.getAppointment().getPatient().getId() == patientId
            )).toList();
            List<MedicalRecordDTO> medicalRecordDTOList = new ArrayList<>();
            for (MedicalRecord medicalRecord : fileteredMedicalRecordList) {
                MedicalRecordDTO medicalRecordDTO = createMedicalRecordDTO(medicalRecord);
                medicalRecordDTO.setAppointmentId(medicalRecord.getAppointment().getId());
                medicalRecordDTO.setDoctorName(medicalRecord.getAppointment().getDoctor().getUser().getName());
                medicalRecordDTOList.add(medicalRecordDTO);
            }
            return ResponseEntity.ok(medicalRecordDTOList);
        }
        throw new ResourceNotFoundException("Please verify the appointment ID and try again");
    }

    private MedicalRecordDTO createMedicalRecordDTO(MedicalRecord medicalRecord) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setDiagnosis(medicalRecord.getDiagnosis());
        dto.setNote(medicalRecord.getNote());
       dto.setRecordCreatedOn(LocalDate.now());
        dto.setMedicineName(medicalRecord.getPrescription());
        return dto;
    }
}
