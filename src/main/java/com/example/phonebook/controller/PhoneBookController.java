package com.example.phonebook.controller;

import com.example.phonebook.dto.ContactInfo;
import com.example.phonebook.service.PhoneBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/contacts")
public class PhoneBookController {

    private final PhoneBookService phoneBookService;

    @Autowired
    public PhoneBookController(PhoneBookService phoneBookService) {
        this.phoneBookService = phoneBookService;
    }

    @GetMapping
    public Map<String, List<String>> getAllContacts() {
        return phoneBookService.getAllContacts();
    }

    @GetMapping("/{name}")
    public List<String> getContact(@PathVariable String name) {
        return phoneBookService.getContact(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addContact(@RequestBody ContactInfo contactInfo) {
        phoneBookService.addContact(contactInfo.getName(), contactInfo.getPhoneNumbers());
    }

    @PutMapping("/{name}")
    public void updateContact(@PathVariable String name, @RequestBody ContactInfo contactInfo) {
        phoneBookService.updateContact(name, contactInfo.getPhoneNumbers());
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeContact(@PathVariable String name) {
        phoneBookService.deleteContact(name);
    }
}
