package com.example.phonebook.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ContactInfo {
    private String name;
    private List<String> phoneNumbers;
}
