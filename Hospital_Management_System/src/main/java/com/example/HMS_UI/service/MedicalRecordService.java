package com.example.Hospital_Management_System.service;

import com.example.Hospital_Management_System.model.MedicalRecord;
import com.example.Hospital_Management_System.repo.MedicalRecordRepo;
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
}