package com.example.phonebook.service;

import com.example.phonebook.exceptions.ContactNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PhoneBookService {
    private final Map<String, List<String>> phoneBook = new HashMap<>();

    // Add a new contact or a new number to an existing contact
    public void addPhoneNumber(String name, String phoneNumber) {
        phoneBook.computeIfAbsent(name, k -> new ArrayList<>()).add(phoneNumber);
    }

    // Get all phone numbers for a given name
    public List<String> getPhoneNumbers(String name) {
        return phoneBook.getOrDefault(name, new ArrayList<>());
    }

    // Get all contacts
    public Map<String, List<String>> getAllContacts() {
        return phoneBook;
    }

    // Remove a specific phone number for a contact
    public boolean removePhoneNumber(String name, String phoneNumber) {
        List<String> numbers = phoneBook.get(name);
        if (numbers != null) {
            boolean removed = numbers.remove(phoneNumber);
            if (numbers.isEmpty()) {
                phoneBook.remove(name); // Remove contact if no numbers are left
            }
            return removed;
        }
        return false;
    }

    // Remove all phone numbers for a contact (remove the contact)
    public void removeContact(String name) {
        if (!phoneBook.containsKey(name)) {
            throw new ContactNotFoundException("Contact with name '" + name + "' does not exist.");
        }
        phoneBook.remove(name);
    }

    // Check if a contact exists
    public boolean contactExists(String name) {
        return phoneBook.containsKey(name);
    }
}
