package com.example.Hospital_Management_System.service;


import com.example.Hospital_Management_System.constant.APPOINTMENT_STATUS;
import com.example.Hospital_Management_System.exception.ResourceNotFoundException;
import com.example.Hospital_Management_System.model.Appointment;
import com.example.Hospital_Management_System.repo.UserRepo;
import com.example.Hospital_Management_System.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserService{
    @Autowired
    private UserRepo userRepo;
    public User save(User user){
        return userRepo.save(user);
    }
    public List<User> findAll(){
        return userRepo.findAll();
    }
    public Optional<User> findById(int id){
        return userRepo.findById(id);
    }
    public Optional<User> findByUserName(String userName){ return userRepo.findByName(userName);}

    public Optional<User> findByEmail(String email) { return userRepo.findByEmail(email); }
}