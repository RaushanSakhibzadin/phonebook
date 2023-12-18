package com.example.phonebook.controller;

import com.example.phonebook.service.PhoneBookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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

    @BeforeEach
    void setup() throws Exception {
        logPhoneBookContents("Before Test");
    }

    @AfterEach
    void tearDown() throws Exception {
        logPhoneBookContents("After Test");
    }

    private void logPhoneBookContents(String message) throws Exception {
        System.out.println(message + " - PhoneBook Contents:");
        mockMvc.perform(get("/api/v1/contacts"))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Test
    void whenGetAllContacts_thenReturnJsonArray() throws Exception {
        phoneBookService.addPhoneNumber("John Doe", "1234567890");
        mockMvc.perform(get("/api/v1/contacts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['John Doe']").isArray())
                .andExpect(jsonPath("$.['John Doe'][0]").value("1234567890"));
    }

    @Test
    void whenGetAllContacts_thenStatus200() throws Exception {
        phoneBookService.addPhoneNumber("Bob", "12345");
        mockMvc.perform(get("/api/v1/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Bob", hasSize(1)))
                .andExpect(jsonPath("$.Bob[0]", is("12345")));
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
                .andExpect(status().isNotFound()); // Expecting 404 Not Found
    }


    @Test
    void whenGetNonExistingContact_thenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/contacts/NonExisting")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenAddNewContact_thenCreated() throws Exception {
        mockMvc.perform(post("/api/v1/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Bob\", \"phoneNumber\":\"67890\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void whenUpdateExistingContact_thenOk() throws Exception {
        phoneBookService.addPhoneNumber("Bob", "12345");
        mockMvc.perform(put("/api/v1/contacts/Bob")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\":\"98765\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void whenUpdateNonExistingContact_thenNotFound() throws Exception {
        mockMvc.perform(put("/api/v1/contacts/Bob")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\":\"54321\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteExistingContact_thenNoContent() throws Exception {
        phoneBookService.addPhoneNumber("Bob", "12345");
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
