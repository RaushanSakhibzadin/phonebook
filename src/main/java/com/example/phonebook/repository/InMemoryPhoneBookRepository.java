package com.example.phonebook.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryPhoneBookRepository implements PhoneBookRepository {

    private final Map<String, List<String>> phoneBook = new HashMap<>();

    @Override
    public Map<String, List<String>> findAll() {
        return new HashMap<>(phoneBook);
    }

    @Override
    public List<String> findByName(String name) {
        return phoneBook.getOrDefault(name, new ArrayList<>());
    }

    @Override
    public void addPhoneNumber(String name, String phoneNumber) {
        phoneBook.computeIfAbsent(name, k -> new ArrayList<>()).add(phoneNumber);
    }

    @Override
    public void remove(String name) {
        phoneBook.remove(name);
    }

    @Override
    public boolean existsByName(String name) {
        return phoneBook.containsKey(name);
    }
}
