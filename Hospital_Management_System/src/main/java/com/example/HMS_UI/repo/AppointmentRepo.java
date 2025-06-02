package com.example.HMS_UI.repo;

import com.example.HMS_UI.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Integer>, PagingAndSortingRepository<Appointment,Integer> {
    @Query(value = "select * from appointment where doctor_id= :id",nativeQuery = true)
    public List<Appointment> findBYDoctorId(@Param("id") int id);

    @Query(value = "select * from appointment where patient_id= :id",nativeQuery = true)
    public List<Appointment> findBYPatientId(@Param("id") int id);
}
