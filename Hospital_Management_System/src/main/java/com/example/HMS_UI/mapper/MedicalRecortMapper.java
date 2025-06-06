package com.example.HMS_UI.mapper;

import com.example.HMS_UI.dto.MedicalRecordDTO;
import com.example.HMS_UI.model.MedicalRecord;

import java.time.LocalDate;

public class MedicalRecortMapper {
    public  MedicalRecordDTO createMedicalRecordDTO(MedicalRecord medicalRecord) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setDiagnosis(medicalRecord.getDiagnosis());
        dto.setNote(medicalRecord.getNote());
        dto.setRecordCreatedOn(LocalDate.now());
        dto.setMedicineName(medicalRecord.getPrescription());
        dto.setDoctorName(medicalRecord.getAppointment().getDoctor().getUser().getName());
        dto.setRecordCreatedOn(medicalRecord.getRecordCreatedOn());
        return dto;
    }
}
