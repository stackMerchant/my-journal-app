package com.svats.journalApp.service;

import com.svats.journalApp.entity.User;
import com.svats.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * Doesn't use Spring's context, use when mocking all beans
 */
public class UserDetailsServiceImplTests {

    @InjectMocks
    UserDetailsServiceImpl UserDetailsService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void loadUserByUsernameTest() {
        when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(User.builder().username("user1").password("XXXuser1XXX").roles(new ArrayList<>()).build()));
        UserDetails user = UserDetailsService.loadUserByUsername("user1");
        Assertions.assertNotNull(user);
    }

}
