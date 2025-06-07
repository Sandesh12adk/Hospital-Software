package com.example.HMS_UI.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.boot.web.servlet.error.ErrorAttributes;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletRequest request, Model model) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("status", 500);
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("exception", ex.getClass().getSimpleName());
        model.addAttribute("path", request.getRequestURI());

        // Optional: include stack trace for dev
        StringBuilder trace = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            trace.append(element).append("\n");
        }
        model.addAttribute("trace", trace.toString());

        return "error"; // Thymeleaf will render error.html
    }
    }