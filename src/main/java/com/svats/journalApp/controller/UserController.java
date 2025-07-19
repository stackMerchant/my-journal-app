package com.svats.journalApp.controller;

import com.svats.journalApp.entity.User;
import com.svats.journalApp.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.svats.journalApp.controller.ControllerHelpers.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return handleException(() -> {
            userService.save(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        });
    }

    @GetMapping("/id/{entryId}")
    public ResponseEntity<User> getById(@PathVariable ObjectId entryId) {
        return handleExceptionOptional(() -> userService.getById(entryId));
    }

    @PutMapping("/id/{entryId}")
    public ResponseEntity<User> updateUser(@RequestBody User newUser) {
        return handleExceptionOptional(() -> userService.updateUser(newUser));
    }

    @DeleteMapping("/id/{entryId}")
    public ResponseEntity<User> deleteById(@PathVariable ObjectId entryId) {
        return handleException(() -> {
            userService.deleteById(entryId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        });
    }

}
