package com.example.HMS_UI.controller;
import com.example.HMS_UI.dto.MedicalRecordDTO;
import com.example.HMS_UI.dto.MedicalRecordSaveDTO;
import com.example.HMS_UI.exception.ResourceNotFoundException;
import com.example.HMS_UI.model.MedicalRecord;
import com.example.HMS_UI.service.AppointmentService;
import com.example.HMS_UI.service.GrantAccess;
import com.example.HMS_UI.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MedicalRecordWebController {

    @Autowired
    private MedicalRecordService medicalRecordService;
    @Autowired
    private AppointmentService appointmentService;
    @GetMapping("/patients/{patientId}/medical-history")
    public String getMedicalHistory(@PathVariable int patientId, Model model) {
        List<MedicalRecordDTO> medicalRecords = medicalRecordService.getMedicalRecordsByPatient(patientId);
        medicalRecords.forEach(medicalRecord -> {System.out.println(medicalRecord);});
        model.addAttribute("medicalRecords", medicalRecords);
        return "medical-history";
    }
    @GetMapping("/medical-record-form")
    public String gerForm(Model model){

        model.addAttribute("medicalRecord",new MedicalRecordSaveDTO());
        int doctorId= new GrantAccess().getAuthenticationUser().getDoctor().getId();
        List<Integer> appointmentIds= medicalRecordService.getAppointmentIdsOfDoctor( doctorId);
        model.addAttribute("appointmentIds", appointmentIds);
        return "medical-record";
    }
    @PostMapping("/doctor/records/create")
    public String submitMedicalRecord(@ModelAttribute("medicalRecord") MedicalRecordSaveDTO dto) {
        MedicalRecord medicalRecord= new MedicalRecord();
        medicalRecord.setNote(dto.getNote());
        medicalRecord.setDiagnosis(dto.getDiagnosis());
        medicalRecord.setPrescription(dto.getPrescription());
        medicalRecord.setAppointment(appointmentService.findById(dto.getAppointmentId()
                ).orElseThrow(()-> new ResourceNotFoundException("Cannot find the appoint with p" +
                "roviced id")));
        medicalRecordService.save(medicalRecord);
        appointmentService.updateAppointmentStatusToCompleted(dto.getAppointmentId());
        System.out.println("saved");
        return "redirect:/user/doctor/dashboard";
    }
}