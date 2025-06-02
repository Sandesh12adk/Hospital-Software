package com.example.HMS_UI.service;

import com.example.HMS_UI.model.User;
import com.example.HMS_UI.service.securityservice.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GrantAccess {
     ;
    public User getAuthenticationUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal= (UserPrincipal) auth.getPrincipal();
        User user= userPrincipal.getUser();
        return user;
    }

}
//Exceptoin:ClassCastException
//Message:class java.lang.String cannot be cast to class com.example.Hospital_Management_System.service.securityservice.UserPrincipal (java.lang.String is in module java.base of loader 'bootstrap'; com.example.Hospital_Management_System.service.securityservice.UserPrincipal is in unnamed module of loader 'app')
//Cause:null