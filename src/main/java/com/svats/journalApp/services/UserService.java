package com.svats.journalApp.services;

import com.svats.journalApp.entity.User;
import com.svats.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> getById(ObjectId entryId) {
        return userRepository.findById(entryId);
    }

    public boolean deleteById(ObjectId entryId) {
        userRepository.deleteById(entryId);
        return true;
    }

    public Optional<User> updateUser(User newUser) {
        return userRepository.findByUsername(newUser.getUsername()).map(oldUser -> {
            oldUser.setPassword(newUser.getPassword());
            userRepository.save(oldUser);
            return newUser;
        });
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
