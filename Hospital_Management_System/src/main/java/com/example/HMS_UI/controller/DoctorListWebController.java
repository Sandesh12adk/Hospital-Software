package com.example.HMS_UI.controller;

import com.example.HMS_UI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DoctorListWebController {
    @Autowired
    private UserService userService;
    @GetMapping("/doctors")
    public String doctors(Model model){
        model.addAttribute("doctors",userService.DoctorDashboardDTOList());
        return "doctor-list";
    }
}
