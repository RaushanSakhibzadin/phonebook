package com.example.phonebook.service;

import com.example.phonebook.repository.InMemoryPhoneBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PhoneBookServiceTest {

    @Mock
    private InMemoryPhoneBookRepository phoneBookRepository;

    @InjectMocks
    private PhoneBookService phoneBookService;

    @BeforeEach
    void setUp() {
        phoneBookService = new PhoneBookService(phoneBookRepository);
    }

    @Test
    void addContact_AddsNewContact() {
        String name = "John Doe";
        String phoneNumber = "1234567890";
        phoneBookService.addContact(name, List.of(phoneNumber));

        verify(phoneBookRepository).addPhoneNumber(name, phoneNumber);
        verify(phoneBookRepository, times(1)).addPhoneNumber(anyString(), anyString());
    }

    @Test
    void updateContact_UpdatesExistingContact() {
        String name = "Alice";
        String phoneNumber = "12345";
        when(phoneBookRepository.existsByName(name)).thenReturn(true);
        when(phoneBookRepository.findByName(name)).thenReturn(Collections.singletonList(phoneNumber));

        phoneBookService.updateContact(name, List.of(phoneNumber));

        verify(phoneBookRepository).addPhoneNumber(name, phoneNumber);
    }

    @Test
    void deleteContact_DeletesExistingContact() {
        String name = "Bob";
        when(phoneBookRepository.existsByName(name)).thenReturn(true);
        when(phoneBookRepository.findByName(name)).thenReturn(Collections.emptyList());

        phoneBookService.deleteContact(name);

        verify(phoneBookRepository).remove(name);
    }
}
