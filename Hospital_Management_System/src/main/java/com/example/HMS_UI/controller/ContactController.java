package com.example.HMS_UI.controller;

import com.example.HMS_UI.dto.ContactRequestDTO;
import com.example.HMS_UI.mapper.ContactMapper;
import com.example.HMS_UI.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contact")
public class ContactController {
 @Autowired
 private ContactService contactService;
    @GetMapping
    public String showContactForm(Model model) {
        model.addAttribute("contactForm", new ContactRequestDTO());
        return "contact";
    }

    @PostMapping("/save")
    public String submitContactForm(@Valid @ModelAttribute("contactForm") ContactRequestDTO contactForm,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "contact";
        }

     contactService.Save(new ContactMapper().RequestDtoToContact(contactForm));

        redirectAttributes.addFlashAttribute("success",
                "Thank you for your message! We'll contact you soon.");
        return "redirect:/contact";
    }
}
