package com.example.HMS_UI.service;

import com.example.HMS_UI.constant.APPOINTMENT_STATUS;
import com.example.HMS_UI.dto.MedicalRecordDTO;
import com.example.HMS_UI.mapper.MedicalRecortMapper;
import com.example.HMS_UI.model.MedicalRecord;
import com.example.HMS_UI.repo.MedicalRecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {
    @Autowired
    private MedicalRecordRepo medicalRecordRepo;

    public MedicalRecord save(MedicalRecord medicalRecord){
        return  medicalRecordRepo.save(medicalRecord);
    }
    public Iterable<MedicalRecord> findAll(){
        return medicalRecordRepo.findAll();
    }

    public List<MedicalRecordDTO> getMedicalRecordsByPatient(int patientId) {
       return medicalRecordRepo.findByAppointmentPatientId(patientId)
                .stream()
                .filter(medicalRecord -> {return medicalRecord.getAppointment()!=null;})
                .filter(medicalRecord -> {return medicalRecord.getAppointment()!=null;})
                .filter(medicalRecord -> {return medicalRecord.getAppointment().getDoctor()!=null;})
                .filter(medicalRecord -> {return medicalRecord.getAppointment().getPatient()!=null;})
                .filter(medicalRecord -> {return medicalRecord.getPrescription()!=null;})
                .filter(medicalRecord -> {return medicalRecord.getDiagnosis()!=null;})
                .map(medicalRecord -> {
                    return new MedicalRecortMapper().createMedicalRecordDTO(medicalRecord);
                }).toList();
    }

    public int getRecordCount(int patientId) {
        return medicalRecordRepo.findByAppointmentPatientId(patientId).size();
    }

    public List<Integer> getAppointmentIdsOfDoctor(int doctorId) {
        return medicalRecordRepo.findByDoctorId(doctorId)
                .stream()
                .filter(appointment -> {return appointment.getStatus()==APPOINTMENT_STATUS.valueOf("SCHEDULDED");})
                .map(appointment -> {return appointment.getId();})
                .toList();
    }
}