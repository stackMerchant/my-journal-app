package com.svats.journalApp.controller;

import com.svats.journalApp.entity.User;
import com.svats.journalApp.scheduler.UserScheduler;
import com.svats.journalApp.service.UserDetailsServiceImpl;
import com.svats.journalApp.service.UserService;
import com.svats.journalApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.svats.journalApp.controller.ControllerHelpers.handleException;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserScheduler userScheduler;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }

    @GetMapping("/trigger-mails")
    public ResponseEntity<String> triggerMails() {
        return handleException(() -> {
            userScheduler.fetchUsersAndSendMail();
            return new ResponseEntity<>("Done", HttpStatus.CREATED);
        });
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        return handleException(() -> {
            userService.createNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        });
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return handleException(() -> {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        });
    }

}
