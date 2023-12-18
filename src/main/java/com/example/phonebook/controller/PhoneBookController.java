package com.example.phonebook.controller;

import com.example.phonebook.dto.PhoneNumberDTO;
import com.example.phonebook.exceptions.ContactNotFoundException;
import com.example.phonebook.service.PhoneBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<Map<String, List<String>>> getAllContacts() {
        return ResponseEntity.ok(phoneBookService.getAllContacts());
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<String>> getContact(@PathVariable String name) {
        if (!phoneBookService.contactExists(name)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(phoneBookService.getPhoneNumbers(name));
    }
    @PostMapping
    public ResponseEntity<Void> addContact(@RequestBody Map<String, String> contact) {
        String name = contact.get("name");
        String phoneNumber = contact.get("phoneNumber");
        phoneBookService.addPhoneNumber(name, phoneNumber);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{name}")
    public ResponseEntity<Void> addPhoneNumber(@PathVariable String name, @RequestBody PhoneNumberDTO phoneNumberDTO) {
        if (!phoneBookService.contactExists(name)) {
            return ResponseEntity.notFound().build();
        }
        phoneBookService.addPhoneNumber(name, phoneNumberDTO.getPhoneNumber());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> removeContact(@PathVariable String name) {
        phoneBookService.removeContact(name);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleContactNotFoundException(ContactNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
}
