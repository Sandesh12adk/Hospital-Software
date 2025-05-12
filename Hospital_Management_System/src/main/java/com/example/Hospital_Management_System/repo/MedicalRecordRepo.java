package com.example.Hospital_Management_System.repo;

import com.example.Hospital_Management_System.model.MedicalRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordRepo extends CrudRepository<MedicalRecord, Integer> {
}
