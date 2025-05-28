package com.example.Hospital_Management_System.controller;

import com.example.Hospital_Management_System.dto.update.PasswordUpdateDTO;
import com.example.Hospital_Management_System.dto.update.emailUpdateDTO;
import com.example.Hospital_Management_System.exception.ResourceNotFoundException;
import com.example.Hospital_Management_System.model.User;
import com.example.Hospital_Management_System.service.UserService;
import com.example.Hospital_Management_System.service.securityservice.UserPrincipal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/update")
@CrossOrigin(origins = "*")
@Tag(name="User Costomization Apis",description= "Change-Password,  Change-Email")
public class UserCostomizationController {
    @Autowired
    private UserService userService;

    @Operation(
            summary = "Update user password",
            description = "Allows an authenticated user to update their password by verifying the old password."
    )

    @PostMapping("/password")
    public ResponseEntity<PasswordUpdateDTO> updatePassword(@Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) throws AccessDeniedException{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        User userr = userPrincipal.getUser();
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(5);
        String encriptedNewPassword= bCrypt.encode(passwordUpdateDTO.getNewPassword());
        String encirptedOldPassword= bCrypt.encode(passwordUpdateDTO.getOldPassword());
        if(auth.isAuthenticated() && encirptedOldPassword.equals(userr.getPassword())) {
            User user = userService.findById(userr.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Error"));
            user.setPassword(encriptedNewPassword);
            userService.save(user);
            return ResponseEntity.ok(passwordUpdateDTO);
        }
         throw new AccessDeniedException("Sorry, Failed to update you password. PLease make sure you old and new " +
                 "password are not matching");
    }
/*
    @PostMapping("/email")
    public ResponseEntity<emailUpdateDTO> updateEmail(@Valid @RequestBody emailUpdateDTO emailUpdateDTO)throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        User userr = userPrincipal.getUser();
        if (userr.getId() == emailUpdateDTO.getUserId()) {
            User user = userService.findById(emailUpdateDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("JPT User Id na hana na"));
            user.setEmail(emailUpdateDTO.getNewEmailId());
            userService.save(user);
            return ResponseEntity.ok(emailUpdateDTO);
        }
        throw new AccessDeniedException("Sorry, Failed to update you Email");
    }
*/
}
