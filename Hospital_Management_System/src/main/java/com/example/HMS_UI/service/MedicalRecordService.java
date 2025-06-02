package com.example.HMS_UI.service;

import com.example.HMS_UI.model.MedicalRecord;
import com.example.HMS_UI.repo.MedicalRecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}