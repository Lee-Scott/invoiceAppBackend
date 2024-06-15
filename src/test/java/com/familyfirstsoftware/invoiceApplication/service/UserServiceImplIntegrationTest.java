package com.familyfirstsoftware.invoiceApplication.service;


import com.familyfirstsoftware.invoiceApplication.dto.UserDTO;

import com.familyfirstsoftware.invoiceApplication.exception.ApiException;
import com.familyfirstsoftware.invoiceApplication.service.implementation.UserServiceImpl;
import com.familyfirstsoftware.invoiceApplication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// Todo: finish this class
@SpringBootTest
public class UserServiceImplIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void nothing() {
        assertEquals(1, 1);
    }
}

