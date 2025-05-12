package com.example.Hospital_Management_System.controller;

import com.example.Hospital_Management_System.constant.USER_ROLE;
import com.example.Hospital_Management_System.dto.AccountInfoDTO;
import com.example.Hospital_Management_System.dto.LoginDTO;
import com.example.Hospital_Management_System.dto.LoginSaveDTO;
import com.example.Hospital_Management_System.model.User;
import com.example.Hospital_Management_System.service.GrantAccess;
import com.example.Hospital_Management_System.service.securityservice.JWTService;
import com.example.Hospital_Management_System.service.securityservice.MyUserDetailsService;
import com.example.Hospital_Management_System.service.securityservice.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@Tag(name="Login Apis",description= "Get_JWT_Token")
public class LoginController {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private GrantAccess grantAccess;

    //permitall
    @PostMapping("/login")
    public ResponseEntity<LoginDTO> returnJWTToken(@RequestBody LoginSaveDTO loginSaveDTO) throws AccessDeniedException {
        String jwtToken = "";
        List<String> roles = new ArrayList<>();
        Authentication auth = jwtService.getAuthenticatontion(loginSaveDTO);
        if (auth.isAuthenticated()) {     // This is the Entry point of Security
            UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
            User user = userPrincipal.getUser();
            jwtToken = jwtService.generateToken(loginSaveDTO.getUserName());
            auth.getAuthorities().forEach((grantedAuthority) ->
                    roles.add(grantedAuthority.getAuthority())
            );
            LoginDTO loginDTO= new LoginDTO();
            loginDTO.setJwtToken(jwtToken);
            loginDTO.setRoles(roles);
            loginDTO.setUserName(user.getName());
            return ResponseEntity.ok(loginDTO);
        }
        throw new AccessDeniedException("Invalid Credientals");
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
                accountInfoDTO.setRoleAndId(role + ":" + roleId);
            }
            return ResponseEntity.ok(accountInfoDTO);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


}