package com.svats.journalApp.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryImplTests {

    @Autowired
    UserRepositoryImpl userRepository;

    @Test
    public void getUsersForAnalysisTest() {
        Assertions.assertNotNull(userRepository.getUsersForAnalysis());
        Assertions.assertEquals(1, userRepository.getUsersForAnalysis().size());
    }
}
