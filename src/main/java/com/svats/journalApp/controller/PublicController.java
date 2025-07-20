package com.svats.journalApp.controller;

import com.svats.journalApp.entity.User;
import com.svats.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.svats.journalApp.controller.ControllerHelpers.handleException;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserService userService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return handleException(() -> {
            userService.createNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        });
    }

}
