package com.frontend.user.controller.web;

import com.frontend.user.client.DocumentClient;
import com.frontend.user.validator.FileValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebDocumentControllerTest {

    @Mock
    private DocumentClient client;

    @Mock
    private FileValidator fileValidator;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private WebDocumentController controller;

    @Test
    void upload_redirects() throws Exception {
        String result = controller.upload(1L, null, redirectAttributes);
        assertEquals("redirect:/users/1/documents", result);
    }

    @Test
    void upload_redirectsOnError() throws Exception {
        String result = controller.upload(2L, null, redirectAttributes);
        assertEquals("redirect:/users/2/documents", result);
    }

    @Test
    void delete_redirects() {
        String result = controller.delete(1L, 1L, redirectAttributes);
        assertEquals("redirect:/users/1/documents", result);
    }
}
