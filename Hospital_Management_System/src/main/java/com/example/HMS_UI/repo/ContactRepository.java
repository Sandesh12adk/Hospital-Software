package com.example.HMS_UI.repo;

import com.example.HMS_UI.constant.CONTACT_STATUS;
import com.example.HMS_UI.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    // 1. Search all (no status)
    @Query("SELECT c FROM Contact c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.subject) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.message) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "ORDER BY c.contactDate DESC")
    List<Contact> searchAll(String search);

    // 2. Search with status
    @Query("SELECT c FROM Contact c WHERE " +
            "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.subject) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.message) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "c.status = :status " +
            "ORDER BY c.contactDate DESC")
    List<Contact> searchWithStatus(String search, CONTACT_STATUS status);

    // 3. Filter by status with pagination
    Page<Contact> findByStatusOrderByContactDateDesc(CONTACT_STATUS status, Pageable pageable);

    // 4. Get all contacts ordered by contactDate descending
    Page<Contact> findAllByOrderByContactDateDesc(Pageable pageable);
}
