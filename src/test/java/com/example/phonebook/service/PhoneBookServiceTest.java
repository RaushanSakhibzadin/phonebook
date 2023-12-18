package com.example.phonebook.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class PhoneBookServiceTest {

    private PhoneBookService phoneBookService;

    @BeforeEach
    void setUp() {
        phoneBookService = new PhoneBookService();
    }

    @Test
    void addAndGetPhoneNumber() {
        String name = "John Doe";
        String phoneNumber = "1234567890";
        phoneBookService.addPhoneNumber(name, phoneNumber);

        List<String> retrievedNumbers = phoneBookService.getPhoneNumbers(name);
        assertTrue(retrievedNumbers.contains(phoneNumber));
    }

    @Test
    void removePhoneNumber() {
        String name = "Jane Doe";
        String phoneNumber = "0987654321";
        phoneBookService.addPhoneNumber(name, phoneNumber);

        boolean isRemoved = phoneBookService.removePhoneNumber(name, phoneNumber);
        assertTrue(isRemoved);
        assertFalse(phoneBookService.contactExists(name));
    }

    @Test
    void whenAddPhoneNumberToNewContact_thenContactIsAdded() {
        phoneBookService.addPhoneNumber("Alice", "12345");
        assertTrue(phoneBookService.contactExists("Alice"));
    }

    @Test
    void whenAddPhoneNumberToExistingContact_thenNumberIsAdded() {
        phoneBookService.addPhoneNumber("Alice", "12345");
        phoneBookService.addPhoneNumber("Alice", "67890");
        assertEquals(2, phoneBookService.getPhoneNumbers("Alice").size());
    }

    @Test
    void whenGetPhoneNumbersOfExistingContact_thenReturnNumbers() {
        phoneBookService.addPhoneNumber("Bob", "11111");
        assertFalse(phoneBookService.getPhoneNumbers("Bob").isEmpty());
    }

    @Test
    void whenGetPhoneNumbersOfNonExistingContact_thenReturnEmptyList() {
        assertTrue(phoneBookService.getPhoneNumbers("NonExisting").isEmpty());
    }

    @Test
    void whenRemovePhoneNumber_thenNumberIsRemoved() {
        phoneBookService.addPhoneNumber("Charlie", "22222");
        phoneBookService.removePhoneNumber("Charlie", "22222");
        assertTrue(phoneBookService.getPhoneNumbers("Charlie").isEmpty());
    }

    @Test
    void whenRemoveLastPhoneNumber_thenContactIsRemoved() {
        phoneBookService.addPhoneNumber("Dave", "33333");
        phoneBookService.removePhoneNumber("Dave", "33333");
        assertFalse(phoneBookService.contactExists("Dave"));
    }

    @Test
    void whenRemovePhoneNumberFromNonExistingContact_thenDoNothing() {
        assertFalse(phoneBookService.removePhoneNumber("NonExisting", "44444"));
    }

    @Test
    void whenRemoveExistingContact_thenContactIsRemoved() {
        phoneBookService.addPhoneNumber("Eve", "55555");
        phoneBookService.removeContact("Eve");
        assertFalse(phoneBookService.contactExists("Eve"));
    }

    @Test
    void whenRemoveNonExistingContact_thenDoNothing() {
        Map<String, List<String>> oldPhoneBook = new HashMap<>(phoneBookService.getAllContacts());
        Map<String, List<String>> newPhoneBook = phoneBookService.getAllContacts();
        assertEquals(oldPhoneBook, newPhoneBook, "Phone book should remain unchanged");
    }


    @Test
    void whenCheckExistenceOfExistingContact_thenReturnTrue() {
        phoneBookService.addPhoneNumber("Frank", "66666");
        assertTrue(phoneBookService.contactExists("Frank"));
    }

    @Test
    void whenCheckExistenceOfNonExistingContact_thenReturnFalse() {
        assertFalse(phoneBookService.contactExists("NonExisting"));
    }

}
