package com.svats.journalApp.controller;

import com.svats.journalApp.services.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.svats.journalApp.controller.ControllerHelpers.handleExceptionOptional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        return handleExceptionOptional(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return userService.updateUser(username, updateUserDTO);
        });
    }

    @Data
    public static class UpdateUserDTO {
        String password;
    }

}
