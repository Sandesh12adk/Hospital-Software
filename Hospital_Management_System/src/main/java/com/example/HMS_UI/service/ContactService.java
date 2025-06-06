package com.example.HMS_UI.service;

import com.example.HMS_UI.constant.CONTACT_STATUS;
import com.example.HMS_UI.dto.ContactDTO;
import com.example.HMS_UI.exception.ResourceNotFoundException;
import com.example.HMS_UI.model.Contact;

import com.example.HMS_UI.repo.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Page<ContactDTO> getAllContacts(Pageable pageable, CONTACT_STATUS status, String search) {
        Page<Contact> contactsPage;

        if (search != null && !search.isEmpty()) {
            List<Contact> contacts;
            if (status != null) {
                contacts = contactRepository.searchWithStatus(search, status)
                        .stream()
                        .filter(contact -> contact.getStatus() == CONTACT_STATUS.OPEN) // Optional, redundant if repo already filters
                        .toList();
            } else {
                contacts = contactRepository.searchAll(search)
                        .stream()
                        .filter(contact -> contact.getStatus() == CONTACT_STATUS.OPEN)
                        .toList();
            }
            contactsPage = new PageImpl<>(contacts, pageable, contacts.size());
        } else {
            // ✅ Always show only OPEN contacts by default
            if (status != null) {
                contactsPage = contactRepository.findByStatusOrderByContactDateDesc(status, pageable);
            } else {
                contactsPage = contactRepository.findByStatusOrderByContactDateDesc(CONTACT_STATUS.OPEN, pageable);
            }
        }

        return contactsPage.map(this::convertToDto);
    }


    public ContactDTO getContactById(int id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        return convertToDto(contact);
    }

    private ContactDTO convertToDto(Contact contact) {
        ContactDTO dto = new ContactDTO();
        dto.setContactId(contact.getContactId());
        dto.setName(contact.getName());
        dto.setMobileno(contact.getMobileno());
        dto.setMessage(contact.getMessage());
        dto.setSubject(contact.getSubject());
        dto.setEmail(contact.getEmail());
        dto.setStatus(contact.getStatus());
        dto.setContactDate(contact.getContactDate());

        if (contact.getUser() != null) {
            dto.setUserId(contact.getUser().getId());
            dto.setUsername(contact.getUser().getName());
        }

        return dto;
    }
    public void save(Contact contact){
        contactRepository.save(contact);
    }

    public void Save(Contact contact) {
        contactRepository.save(contact);
    }
    public void closeMessage(int contactId){
        Contact contact= contactRepository.findById(contactId).orElseThrow(()-> new ResourceNotFoundException("" +
                "Cannot find the contact with procided id"));
        contact.setStatus(CONTACT_STATUS.CLOSE);
        contactRepository.save(contact);
    }
}