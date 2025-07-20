package com.svats.journalApp.service;


import com.svats.journalApp.entity.User;
import com.svats.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * This uses Spring's context, actual beans and mocks can be tested
 */

@SpringBootTest
public class UserDetailsServiceImplTests2  {

    @Autowired
    UserDetailsServiceImpl UserDetailsService;

    @MockBean
    UserRepository userRepository;

    @Test
    public void loadUserByUsernameTest() {
        when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(User.builder().username("user1").password("XXXuser1XXX").roles(new ArrayList<>()).build()));
        UserDetails user = UserDetailsService.loadUserByUsername("user1");
        Assertions.assertNotNull(user);
    }

}
