package com.frontend.documents.controller.api;

import com.frontend.documents.dto.DocumentDownloadDTO;
import com.frontend.documents.dto.DocumentResponseDTO;
import com.frontend.documents.service.DocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/documents")
public class DocumentController {
    private final DocumentService service;

    public DocumentController(DocumentService service){
        this.service = service;
    }

    @GetMapping("/health")
    public String health() {
        return "Document service is UP!";
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponseDTO>> getAllDocuments(@PathVariable Long userId){

        return ResponseEntity.ok(service.getAllDocumentsByUserID(userId));
    }

    @PostMapping("/upload")
    public ResponseEntity<DocumentResponseDTO> upload(@PathVariable Long userId, @RequestParam("file") MultipartFile file){

        DocumentResponseDTO saved = service.uploadDocument(userId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<byte[]> download(@PathVariable Long userId, @PathVariable Long documentId) {

        DocumentDownloadDTO download = service.downloadDocument(userId, documentId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + download.getOriginalName() + "\"")
                .contentType(MediaType.parseMediaType(download.getContentType()))
                .body(download.getFileContent());
    }


    @DeleteMapping("/delete/{documentId}")
    public ResponseEntity<String> delete(@PathVariable Long userId, @PathVariable Long documentId){
        service.deleteDocument(userId, documentId);

        return ResponseEntity.ok("Successfully deleted!");
    }

    @GetMapping("/find/{documentId}")
    public ResponseEntity<DocumentResponseDTO> findById(@PathVariable Long userId, @PathVariable Long documentId){
        DocumentResponseDTO document = service.findById(userId, documentId);

        return ResponseEntity.ok(document);
    }
}
