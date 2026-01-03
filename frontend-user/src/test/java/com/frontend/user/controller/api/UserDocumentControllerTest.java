package com.frontend.user.controller.api;

import com.frontend.user.client.DocumentClient;
import com.frontend.user.validator.FileValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDocumentControllerTest {

    @Mock
    private DocumentClient client;

    @Mock
    private FileValidator fileValidator;

    @InjectMocks
    private UserDocumentController controller;

    @Test
    void upload_returnsSomething() {
        // просто извикваме метода с null като “файл”
        ResponseEntity<?> response = controller.upload(1L, null);
        assertNotNull(response);
    }

    @Test
    void delete_returnsSomething() {
        ResponseEntity<?> response = controller.delete(1L, 1L);
        assertNotNull(response);
    }
}
