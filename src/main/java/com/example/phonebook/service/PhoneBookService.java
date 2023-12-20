package com.example.phonebook.service;

import com.example.phonebook.exceptions.ContactNotFoundException;
import com.example.phonebook.repository.InMemoryPhoneBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PhoneBookService {

    private final InMemoryPhoneBookRepository phoneBookRepository;

    @Autowired
    public PhoneBookService(InMemoryPhoneBookRepository phoneBookRepository) {
        this.phoneBookRepository = phoneBookRepository;
    }

    public Map<String, List<String>> getAllContacts() {
        return phoneBookRepository.findAll();
    }

    public List<String> getContact(String name) {
        if (contactNotExists(name)) {
            throw new ContactNotFoundException("Contact with name '" + name + "' does not exist.");
        }
        return phoneBookRepository.findByName(name);
    }

    public void addContact(String name, List<String> phoneNumbers) {
        phoneNumbers.forEach(phoneNumber -> phoneBookRepository.addPhoneNumber(name, phoneNumber));
    }

    public void updateContact(String name, List<String> phoneNumbers) {
        if (contactNotExists(name)) {
            throw new ContactNotFoundException("Contact with name '" + name + "' does not exist.");
        }
        phoneNumbers.forEach(phoneNumber -> phoneBookRepository.addPhoneNumber(name, phoneNumber));
    }

    public void deleteContact(String name) {
        if (contactNotExists(name)) {
            throw new ContactNotFoundException("Contact with name '" + name + "' does not exist.");
        }
        phoneBookRepository.remove(name);
    }

    public boolean contactNotExists(String name) {
        return !phoneBookRepository.existsByName(name);
    }
}
