package com.example.HMS_UI.mapper;

import com.example.HMS_UI.constant.CONTACT_STATUS;
import com.example.HMS_UI.dto.ContactRequestDTO;
import com.example.HMS_UI.model.Contact;
import com.example.HMS_UI.service.GrantAccess;
import com.example.HMS_UI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class ContactMapper {

    public Contact RequestDtoToContact(ContactRequestDTO contactRequestDTO){
        Contact contact= new Contact();
        contact.setMobileno(contactRequestDTO.getMobileno());
        contact.setEmail(contactRequestDTO.getEmail());
        contact.setMessage(contactRequestDTO.getMessage());
        contact.setSubject(contactRequestDTO.getSubject());
        contact.setUser(new GrantAccess().getAuthenticationUser());
        contact.setName(new GrantAccess().getAuthenticationUser().getName());
        contact.setStatus(CONTACT_STATUS.OPEN);
        return contact;
    }
}
