package com.example.HMS_UI.controller;

import com.example.HMS_UI.constant.USER_ROLE;
import com.example.HMS_UI.dto.AccountInfoDTO;
import com.example.HMS_UI.dto.LoginDTO;
import com.example.HMS_UI.dto.LoginSaveDTO;
import com.example.HMS_UI.model.User;
import com.example.HMS_UI.service.GrantAccess;
import com.example.HMS_UI.service.securityservice.JWTService;
import com.example.HMS_UI.service.securityservice.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Controller
@CrossOrigin(origins = "*")
@Tag(
        name = "Login APIs",
        description = "Endpoints for user authentication and getting JWT token and retrieving authenticated user's account information."
)
public class LoginController {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private GrantAccess grantAccess;

    //permitall
                    // final
    @GetMapping("/login")
   public String login(){
        return "login";
    }


    @GetMapping("/accountinfo")
    public ResponseEntity<AccountInfoDTO> myAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()) {
            List<String> roles = new ArrayList<>();
            UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
            User user = userPrincipal.getUser();
            AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
            accountInfoDTO.setUserName(user.getName());
            accountInfoDTO.setUserId(user.getId());
            auth.getAuthorities().forEach((grantedauthorities) ->
                    roles.add(grantedauthorities.getAuthority())
            );
            accountInfoDTO.setRoles(roles);
            if (user.getRole() != USER_ROLE.ADMIN) {
                String role = roles.contains("ROLE_DOCTOR") ? "DOCTOR" : "PATIENT";
                int roleId = user.getRole() == USER_ROLE.DOCTOR ? user.getDoctor().getId() : user.getPatient().getId();
                accountInfoDTO.setRoleId(roleId);
            }
            return ResponseEntity.ok(accountInfoDTO);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


}