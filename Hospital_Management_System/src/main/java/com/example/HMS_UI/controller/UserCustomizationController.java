package com.example.Hospital_Management_System.controller;

import com.example.Hospital_Management_System.dto.update.PasswordUpdateDTO;
import com.example.Hospital_Management_System.model.User;
import com.example.Hospital_Management_System.service.UserService;
import com.example.Hospital_Management_System.service.securityservice.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/update")
@CrossOrigin(origins = "*")
@Tag(name = "User Customization APIs", description = "Change-Password, Change-Email")
public class UserCustomizationController {

    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(5);

    @Operation(
            summary = "Update user password",
            description = "Allows an authenticated user to update their password by verifying the old password.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful Operation"),
                    @ApiResponse(responseCode = "400", description = "Bad Request - Invalid ID or parameters"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - You do not have permission to access this resource"),
                    @ApiResponse(responseCode = "404", description = "Not Found - No resource found with given ID"),
                    @ApiResponse(responseCode = "409", description = "Conflict - Resource already exists or violates constraints"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")}
    )
    @PostMapping("/password")
    public ResponseEntity<PasswordUpdateDTO> updatePassword(@Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        User user = userPrincipal.getUser();

        // Verify old password matches stored hashed password
        if (!bCrypt.matches(passwordUpdateDTO.getOldPassword(), user.getPassword())) {
            throw new AccessDeniedException("Old password is incorrect.");
        }

        // Prevent new password from being same as old password
        if (bCrypt.matches(passwordUpdateDTO.getNewPassword(), user.getPassword())) {
            throw new AccessDeniedException("New password must be different from the old password.");
        }

        // Encode and update password
        user.setPassword(bCrypt.encode(passwordUpdateDTO.getNewPassword()));
        userService.save(user);

        return ResponseEntity.ok(passwordUpdateDTO);
    }

    /*
    @PostMapping("/email")
    public ResponseEntity<emailUpdateDTO> updateEmail(@Valid @RequestBody emailUpdateDTO emailUpdateDTO) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        User user = userPrincipal.getUser();

        if (user.getId() == emailUpdateDTO.getUserId()) {
            User userToUpdate = userService.findById(emailUpdateDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with provided ID"));
            userToUpdate.setEmail(emailUpdateDTO.getNewEmailId());
            userService.save(userToUpdate);
            return ResponseEntity.ok(emailUpdateDTO);
        }

        throw new AccessDeniedException("Sorry, failed to update your email.");
    }
    */
}
