package com.frontend.documents.controller.api;

import com.frontend.documents.dto.DocumentResponseDTO;
import com.frontend.documents.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController controller;

    @Test
    void health_returnsCorrectMessage() {
        String result = controller.health(1L);

        assertEquals("Document service is UP for user 1", result);
    }

    @Test
    void getAllDocuments_returnsOkAndBody() {
        when(documentService.getAllDocumentsByUserID(1L))
                .thenReturn(List.of());

        ResponseEntity<List<DocumentResponseDTO>> response =
                controller.getAllDocuments(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull("Response body should not be null!", response.getBody());
    }

    @Test
    void findById_returnsDocument() {
        DocumentResponseDTO dto =
                new DocumentResponseDTO(1L, 1L, "file.txt", 10L, "text/plain", null);

        when(documentService.findById(1L, 1L)).thenReturn(dto);

        ResponseEntity<DocumentResponseDTO> response =
                controller.findById(1L, 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("file.txt", response.getBody().originalFilename());
    }
}
