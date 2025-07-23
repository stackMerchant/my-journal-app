package com.svats.journalApp.controller;

import com.svats.journalApp.entity.User;
import com.svats.journalApp.scheduler.UserScheduler;
import com.svats.journalApp.service.UserDetailsServiceImpl;
import com.svats.journalApp.service.UserService;
import com.svats.journalApp.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
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
    public ResponseEntity<String> signup(@RequestBody CreateUserDTO createUserDTO) {
        return handleException(() -> {
            userService.createNewUser(createUserDTO.toUser());
            return new ResponseEntity<>("User created", HttpStatus.CREATED);
        });
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginUserDTO loginUserDTO) {
        return handleException(() -> {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDTO.getUsername(), loginUserDTO.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginUserDTO.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        });
    }

    @Data
    @AllArgsConstructor
    private static class CreateUserDTO {
        @NonNull
        private String username;

        @NonNull
        private String password;

        private String email;
        private boolean wantsAnalysis;

        public User toUser() {
            return User.builder()
                    .username(username).password(password)
                    .email(email).wantsAnalysis(wantsAnalysis)
                    .build();
        }
    }

    @Data
    private static class LoginUserDTO {
        @NonNull
        private String username;

        @NonNull
        private String password;
    }

}
