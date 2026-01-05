package com.frontend.documents.service;

import com.frontend.documents.dto.DocumentResponseDTO;
import com.frontend.documents.model.Document;
import com.frontend.documents.repository.DocumentRepository;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository repository;

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private DocumentService service;

    @Test
    void findById_returnsDto() {
        Document document = new Document();
        document.setId(1L);
        document.setUserId(1L);
        document.setOriginalFilename("test.txt");

        when(repository.findByUserIdAndId(1L, 1L))
                .thenReturn(Optional.of(document));

        DocumentResponseDTO result = service.findById(1L, 1L);

        assertEquals("Equal names expected!", "test.txt", result.originalFilename());
    }

    @Test
    void findById_throwsException_whenNotFound() {
        when(repository.findByUserIdAndId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.findById(1L, 1L));
    }

    @Test
    void getAllDocumentsByUserID_returnsList() {
        when(repository.findAllByUserId(1L))
                .thenReturn(List.of());

        List<DocumentResponseDTO> result =
                service.getAllDocumentsByUserID(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
