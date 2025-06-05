package com.example.HMS_UI.repo;

import com.example.HMS_UI.model.Appointment;
import com.example.HMS_UI.model.Patient;
import com.example.HMS_UI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository<Patient,Integer>, PagingAndSortingRepository<Patient,Integer> {
    @Query("SELECT p FROM Patient p JOIN FETCH p.user WHERE p.user.id = :userId")
    Optional<Patient> findByUserIdWithUser(@Param("userId") int userId);
}
