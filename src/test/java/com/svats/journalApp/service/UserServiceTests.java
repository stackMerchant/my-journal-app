package com.svats.journalApp.service;

import com.svats.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    UserRepository userRepository;

    @Disabled
    @Test
    public void testsFindByUsername() {
        assertNotNull(userRepository.findByUsername("user1").orElse(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "user1",
            "user2",
            "user3",
            "user4"
    })
    public void test(String username) {
        assertNotNull(userRepository.findByUsername(username).orElse(null));
    }

}
