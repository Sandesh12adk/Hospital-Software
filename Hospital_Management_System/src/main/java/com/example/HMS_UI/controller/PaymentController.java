package com.example.HMS_UI.controller;

import com.example.HMS_UI.exception.ResourceNotFoundException;
import com.example.HMS_UI.model.Appointment;
import com.example.HMS_UI.model.Payment;
import com.example.HMS_UI.repo.AppointmentRepo;
import com.example.HMS_UI.repo.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    @GetMapping("/pay/{id}")
    public String showPaymentForm(@PathVariable int id, Model model) {
        Appointment appointment = appointmentRepo.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Cannot find the appointment with provided id"));
        model.addAttribute("appointmentId", id);
        model.addAttribute("amount",500); // or fixed value
        return "payment-form";
    }

    @PostMapping("/process")
    public String processPayment(@RequestParam("appointmentId") int appointmentId,
                                 @RequestParam("amount") double amount,
                                 @RequestParam("method") String method,
                                 @RequestParam(value = "remarks", required = false) String remarks) {

        Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow();

        Payment payment = new Payment();
        payment.setMethod(method);
        payment.setAmount(amount);
        payment.setStatus("SUCCESS"); // Always success for dummy
        payment.setPaymentDate(LocalDateTime.now());
        payment.setAppointment(appointment);
        paymentRepo.save(payment);
        appointmentRepo.markAppointmentAsPaid(appointmentId);
        return "payment-success";
    }
}
