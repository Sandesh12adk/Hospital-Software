package com.example.HMS_UI.service;


import com.example.HMS_UI.repo.UserRepo;
import com.example.HMS_UI.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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