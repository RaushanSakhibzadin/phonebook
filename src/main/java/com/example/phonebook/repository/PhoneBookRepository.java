package com.example.phonebook.repository;

import java.util.List;
import java.util.Map;

public interface PhoneBookRepository {

    Map<String, List<String>> findAll();

    List<String> findByName(String name);

    void addPhoneNumber(String name, String phoneNumber);

    void remove(String name);

    boolean existsByName(String name);
}
