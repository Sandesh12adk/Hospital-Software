package com.example.Hospital_Management_System.repo;

import com.example.Hospital_Management_System.model.Doctor;
import com.example.Hospital_Management_System.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends PagingAndSortingRepository<User, Integer>, JpaRepository<User,Integer> {
    Optional<User> findByName(String userName);
    @Query(value = "SELECT * FROM users WHERE uuser_id= :userId",nativeQuery = true)
    Doctor findByUserId(@Param("userId") int userId);
}
