package com.svats.journalApp.services;

import com.svats.journalApp.controller.UserController;
import com.svats.journalApp.entity.JournalEntry;
import com.svats.journalApp.entity.User;
import com.svats.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void createNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList("USER"));
        userRepository.save(user);
    }

    public Optional<Boolean> updateUser(String username, UserController.UpdateUserDTO updateUserDTO) {
        return userRepository.findByUsername(username).map(user -> {
            if (updateUserDTO.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
            }
            userRepository.save(user);
            return true;
        });
    }

    public void addJournalEntriesToUser(User user, JournalEntry savedEntry) {
        user.getJournalEntries().add(savedEntry); // Add reference for user
        userRepository.save(user); // Save updated user
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
