package com.example.HMS_UI.service.securityservice;

import com.example.HMS_UI.model.User;
import com.example.HMS_UI.service.UserService;
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
