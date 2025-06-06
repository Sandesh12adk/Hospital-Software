package com.example.HMS_UI.repo;

import com.example.HMS_UI.model.MedicalRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepo extends CrudRepository<MedicalRecord, Integer> {
    List<MedicalRecord> findByAppointmentPatientId(int patientId);
}
