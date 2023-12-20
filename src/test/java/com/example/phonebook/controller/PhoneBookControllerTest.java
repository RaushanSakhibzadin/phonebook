package com.example.phonebook.controller;

import com.example.phonebook.service.PhoneBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PhoneBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PhoneBookService phoneBookService;

    @Test
    void whenGetAllContacts_thenReturnJsonArray() throws Exception {
        phoneBookService.addContact("John Doe", List.of("1234567890"));
        mockMvc.perform(get("/api/v1/contacts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['John Doe']").isArray())
                .andExpect(jsonPath("$.['John Doe'][0]").value("1234567890"));
    }

    @Test
    void whenGetAllContacts_thenStatus200() throws Exception {
        phoneBookService.addContact("Bob", List.of("12345"));
        mockMvc.perform(get("/api/v1/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Bob").isArray())
                .andExpect(jsonPath("$.Bob[0]").value("12345"));
    }

    @Test
    void whenGetNonExistingContact_thenStatus404() throws Exception {
        mockMvc.perform(get("/api/v1/contacts/NonExisting"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetSpecificContact_thenReturnJson() throws Exception {
        mockMvc.perform(get("/api/v1/contacts/Alice")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenAddNewContact_thenCreated() throws Exception {
        Map<String, Object> contactInfo = new HashMap<>();
        contactInfo.put("name", "Bob");
        contactInfo.put("phoneNumbers", List.of("67890"));

        mockMvc.perform(post("/api/v1/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contactInfo)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenUpdateExistingContact_thenOk() throws Exception {
        phoneBookService.addContact("Bob", List.of("12345"));

        Map<String, Object> contactInfo = new HashMap<>();
        contactInfo.put("phoneNumbers", List.of("98765"));

        mockMvc.perform(put("/api/v1/contacts/Bob")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contactInfo)))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteExistingContact_thenNoContent() throws Exception {
        phoneBookService.addContact("Bob", List.of("12345"));
        mockMvc.perform(delete("/api/v1/contacts/Bob")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteNonExistingContact_thenNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/contacts/NonExisting")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
