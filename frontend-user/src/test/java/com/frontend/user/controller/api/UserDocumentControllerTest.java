package com.frontend.user.controller.api;

import com.frontend.user.client.DocumentClient;
import com.frontend.user.client.client.dto.DocumentResponseDTO;
import com.frontend.user.validator.FileValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDocumentControllerTest {

    private DocumentClient documentClient;
    private FileValidator fileValidator;
    private UserDocumentController controller;

    @BeforeEach
    void setUp() {
        documentClient = mock(DocumentClient.class);
        fileValidator = mock(FileValidator.class);
        controller = new UserDocumentController(documentClient, fileValidator);
    }

    @Test
    void getAllDocuments_Success() {
        LocalDateTime now = LocalDateTime.now();
        List<DocumentResponseDTO> documents = List.of(new DocumentResponseDTO(1L, 123L, "file1.pdf", 1024L, "application/pdf", now));
        when(documentClient.getAllDocuments(123L)).thenReturn(documents);

        ResponseEntity<List<DocumentResponseDTO>> response = controller.getAllDocuments(123L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllDocuments_EmptyList() {
        when(documentClient.getAllDocuments(123L)).thenReturn(List.of());

        ResponseEntity<List<DocumentResponseDTO>> response = controller.getAllDocuments(123L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void upload_Success() {
        MultipartFile file = mock(MultipartFile.class);
        LocalDateTime now = LocalDateTime.now();
        DocumentResponseDTO document = new DocumentResponseDTO(1L, 123L, "test.pdf", 1024L, "application/pdf", now);
        doNothing().when(fileValidator).validateFileContent(file);
        when(documentClient.upload(123L, file)).thenReturn(document);

        ResponseEntity<?> response = controller.upload(123L, file);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(document, response.getBody());
    }

    @Test
    void upload_ValidationFails() {
        MultipartFile file = mock(MultipartFile.class);
        doThrow(new RuntimeException("Invalid file")).when(fileValidator).validateFileContent(file);

        ResponseEntity<?> response = controller.upload(123L, file);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid file", response.getBody());
    }

    @Test
    void download_Success() {
        byte[] content = "file content".getBytes();
        LocalDateTime now = LocalDateTime.now();
        DocumentResponseDTO document = new DocumentResponseDTO(1L, 123L, "test.pdf", 1024L, "application/pdf", now);
        when(documentClient.download(123L, 1L)).thenReturn(content);
        when(documentClient.findById(123L, 1L)).thenReturn(document);

        ResponseEntity<byte[]> response = controller.download(123L, 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(content, response.getBody());
    }

    @Test
    void download_NotFound() {
        when(documentClient.download(123L, 999L)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<byte[]> response = controller.download(123L, 999L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void delete_Success() {
        when(documentClient.delete(123L, 1L)).thenReturn("Deleted successfully");

        ResponseEntity<String> response = controller.delete(123L, 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Deleted successfully", response.getBody());
    }

    @Test
    void findById_Success() {
        LocalDateTime now = LocalDateTime.now();
        DocumentResponseDTO document = new DocumentResponseDTO(1L, 123L, "test.pdf", 1024L, "application/pdf", now);
        when(documentClient.findById(123L, 1L)).thenReturn(document);

        ResponseEntity<DocumentResponseDTO> response = controller.findById(123L, 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(document, response.getBody());
    }

    @Test
    void findById_NotFound() {
        when(documentClient.findById(123L, 999L)).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<DocumentResponseDTO> response = controller.findById(123L, 999L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void constructor_Works() {
        assertNotNull(controller);
    }
}