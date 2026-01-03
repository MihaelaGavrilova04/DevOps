package com.frontend.user.client;

import com.frontend.user.client.clientDTO.DocumentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@FeignClient(name = "document-client", url = "${DOCUMENTS_SERVICE_URL}/api")
public interface DocumentClient {

    @GetMapping("/users/{userId}/documents")
    List<DocumentResponseDTO> getAllDocuments(@PathVariable Long userId);

    @PostMapping(value = "/users/{userId}/documents/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    DocumentResponseDTO upload(@PathVariable Long userId, @RequestPart("file") MultipartFile file);

    @GetMapping("/users/{userId}/documents/download/{documentId}")
    byte[] download(@PathVariable Long userId, @PathVariable Long documentId);


    @DeleteMapping("/users/{userId}/documents/delete/{documentId}")
    String delete(@PathVariable Long userId, @PathVariable Long documentId);

    @GetMapping("/users/{userId}/documents/find/{documentId}")
    DocumentResponseDTO findById(@PathVariable Long userId, @PathVariable Long documentId);
}
