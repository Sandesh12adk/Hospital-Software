package com.example.HMS_UI.repo;

import com.example.HMS_UI.constant.APPOINTMENT_STATUS;
import com.example.HMS_UI.model.Appointment;
import com.example.HMS_UI.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Integer>, PagingAndSortingRepository<Appointment,Integer> {
    @Query(value = "select * from appointment where doctor_id= :id",nativeQuery = true)
    public List<Appointment> findBYDoctorId(@Param("id") int id);

    @Query(value = "select * from appointment where patient_id= :id",nativeQuery = true)
    public List<Appointment> findBYPatientId(@Param("id") int id);
    @Query("SELECT a FROM Appointment a " +
            "JOIN FETCH a.doctor d " +
            "JOIN FETCH d.user " +
            "JOIN FETCH d.department " +
            "WHERE a.patient.id = :patientId " +
            "AND a.status IN ('SCHEDULED', 'PENDING') " +
            "AND a.date >= :currentDate " +
            "ORDER BY a.date ASC, a.time ASC")
    List<Appointment> findUpcomingAppointments(@Param("patientId") int patientId,
                                               @Param("currentDate") LocalDate currentDate);

    @Query("SELECT a FROM Appointment a " +
            "JOIN FETCH a.doctor d " +
            "JOIN FETCH d.user " +
            "JOIN FETCH d.department " +
            "WHERE a.patient.id = :patientId " +
            "AND (a.status IN ('COMPLETED', 'CANCELLED') OR a.date < :currentDate) " +
            "ORDER BY a.date DESC, a.time DESC")
    List<Appointment> findPastAppointments(@Param("patientId") int patientId,
                                           @Param("currentDate") LocalDate currentDate);
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId " +
            "AND (:status IS NULL OR a.status = :status) " +
            "AND (:fromDate IS NULL OR a.date >= :fromDate) " +
            "AND (:toDate IS NULL OR a.date <= :toDate)")
    List<Appointment> findFilteredAppointments(@Param("patientId") int patientId,
                                               @Param("status") APPOINTMENT_STATUS status,
                                               @Param("fromDate") LocalDate fromDate,
                                               @Param("toDate") LocalDate toDate);
    List<Appointment> findByPatientAndDateGreaterThanEqual(Patient patient, LocalDate date);

}
