package com.example.Hospital_Management_System.controller;

import com.example.Hospital_Management_System.constant.USER_ROLE;
import com.example.Hospital_Management_System.dto.AdminSaveDTO;
import com.example.Hospital_Management_System.model.User;
import com.example.Hospital_Management_System.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*") // Only for local development!
@Tag(name="Admin APIs",description= "Register")
public class AdminController {
    @Autowired
    private UserService userService;
    //Admin only
    @PostMapping("/register")
    public ResponseEntity<String> saveAdmin(@Valid @RequestBody AdminSaveDTO adminSaveDTO) {
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(5);
        User user= new User();
        user.setRole(USER_ROLE.ADMIN);
        user.setPassword(encoder.encode(adminSaveDTO.getPassword()));
        user.setEmail(adminSaveDTO.getEmail());
        user.setName(adminSaveDTO.getName());
        userService.save(user);
        return ResponseEntity.ok("Saved");
    }
}