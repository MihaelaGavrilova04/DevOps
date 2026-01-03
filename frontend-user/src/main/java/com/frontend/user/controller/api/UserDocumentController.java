package com.frontend.user.controller.api;

import com.frontend.user.client.DocumentClient;
import com.frontend.user.client.clientDTO.DocumentResponseDTO;
import com.frontend.user.validator.FileValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import java.util.List;

//Ако просто пренасочваш Feign client-a и искаш да връщаш отговорите директно, raw типове са достатъчни.
@RestController
@RequestMapping("/api/users/{userId}/documents")
public class UserDocumentController {

    private final DocumentClient client;
    private final FileValidator fileValidator;

    public UserDocumentController(DocumentClient client, FileValidator fileValidator){
        this.client = client;
        this.fileValidator = fileValidator;
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponseDTO>> getAllDocuments(@PathVariable Long userId){
        return ResponseEntity.ok(client.getAllDocuments(userId));
    }

//    ResponseEntity<DocumentResponseDTO> => ResponseEntity<?> because we will return a String when an error occurs
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@PathVariable Long userId, @RequestParam("file") MultipartFile file){
        try{
            fileValidator.validateFileContent(file);
            return ResponseEntity.ok(client.upload(userId, file));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//  attachment => това не е за показване в браузъра - запази го като файл!
// filename => името под което ще бъде записан файла след теглене

    @GetMapping("/download/{documentId}")
    public ResponseEntity<byte[]> download(@PathVariable Long userId, @PathVariable Long documentId) {
        try{
            byte[] content = client.download(userId, documentId);
            DocumentResponseDTO found = client.findById(userId, documentId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = \"" + found.originalFilename() + "\"")
                    .contentType(MediaType.parseMediaType(found.contentType())).body(content);
        }catch (Exception e){
            return ResponseEntity.notFound().build(); // 404
        }
    }


    @DeleteMapping("/delete/{documentId}")
    public ResponseEntity<String> delete(@PathVariable Long userId, @PathVariable Long documentId){
        return ResponseEntity.ok(client.delete(userId, documentId));
    }

    @GetMapping("/find/{documentId}")
    public ResponseEntity<DocumentResponseDTO> findById(@PathVariable Long userId, @PathVariable Long documentId){
        try{
            DocumentResponseDTO document = client.findById(userId, documentId);
            return ResponseEntity.ok(document);
        }catch(Exception e){
            return ResponseEntity.notFound().build(); // 404
        }
    }
}
