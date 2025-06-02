package com.example.Hospital_Management_System.service.securityservice;

import com.example.Hospital_Management_System.model.User;
import com.example.Hospital_Management_System.repo.UserRepo;
import com.example.Hospital_Management_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user= userService.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
        return new UserPrincipal(user);

    }
}
