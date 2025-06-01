package com.example.Hospital_Management_System.controller;

import com.example.Hospital_Management_System.constant.USER_ROLE;
import com.example.Hospital_Management_System.dto.AdminSaveDTO;
import com.example.Hospital_Management_System.model.User;
import com.example.Hospital_Management_System.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*") // Only for local development!
@Tag(name="Admin APIs")
public class AdminController {
    @Autowired
    private UserService userService;
    //Admin only
    @Operation(
            summary = "Register new admin",
            description = "Accessible by ADMIN only",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid ID or parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
            @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Not Found - No resource found with given ID"),
            @ApiResponse(responseCode = "409", description = "Conflict - Resource already exists or violates constraints"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    }
    )
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