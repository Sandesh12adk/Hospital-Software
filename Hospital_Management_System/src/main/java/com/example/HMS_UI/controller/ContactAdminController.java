package com.example.HMS_UI.controller;

import com.example.HMS_UI.constant.CONTACT_STATUS;
import com.example.HMS_UI.dto.ContactDTO;
import com.example.HMS_UI.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/contacts")
public class ContactAdminController {

    private final ContactService contactService;
    private final int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    public ContactAdminController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public String listContacts(Model model,
                               @RequestParam(required = false) String search,
                               @RequestParam(required = false) CONTACT_STATUS status,
                               @RequestParam(defaultValue = "1") int page) {

        Pageable pageable = PageRequest.of(page - 1, DEFAULT_PAGE_SIZE, Sort.by("contactDate").descending());
        Page<ContactDTO> contactsPage = contactService.getAllContacts(pageable, status, search);

        model.addAttribute("contacts", contactsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contactsPage.getTotalPages());
        model.addAttribute("searchQuery", search);
        model.addAttribute("statusFilter", status);

        return "admin/contacts";
    }



    @GetMapping("/close/{id}")
    public String close(@PathVariable int id){
        System.out.println(id);
        contactService.closeMessage(id);
        return "redirect:/admin/contacts";
    }
}