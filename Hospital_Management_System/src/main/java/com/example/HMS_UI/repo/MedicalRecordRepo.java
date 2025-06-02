package com.example.HMS_UI.repo;

import com.example.HMS_UI.model.MedicalRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordRepo extends CrudRepository<MedicalRecord, Integer> {
}
