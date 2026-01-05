package com.frontend.user.controller.web;

import com.frontend.user.client.DocumentClient;
import com.frontend.user.client.client.dto.DocumentResponseDTO;
import com.frontend.user.validator.FileValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users/{userId}/documents")
public class WebDocumentController {
    private final DocumentClient client;
    private final FileValidator fileValidator;

    public WebDocumentController(DocumentClient client, FileValidator fileValidator) {
        this.client = client;
        this.fileValidator = fileValidator;
    }

    @GetMapping
    public String getAllDocuments(@PathVariable Long userId, Model model) {
        try {
            List<DocumentResponseDTO> allDocuments = client.getAllDocuments(userId);
            model.addAttribute("allDocuments", allDocuments);
            model.addAttribute("userId", userId);

        } catch (Exception e) {
            model.addAttribute("error", "Failed to load documents:");
        }

        return "documentsFolder/list";
    }

    @PostMapping("/upload")
    public String upload(@PathVariable Long userId,
                         @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        try {
            fileValidator.validateFileContent(file);
            DocumentResponseDTO uploaded = client.upload(userId, file);
            redirectAttributes.addFlashAttribute("success", "File uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        // Това НЕ сочи към HTML файл, а към URL адрес, който се мапва към друг контролер!

        return "redirect:/users/" + userId + "/documents"; // връщаме се към списъка с документи
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<byte[]> download(@PathVariable Long userId, @PathVariable Long documentId) {
        byte[] content = client.download(userId, documentId);
        DocumentResponseDTO doc = client.findById(userId, documentId);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + doc.originalFilename() + "\"")
                .contentType(MediaType.parseMediaType(doc.contentType())).body(content);
    }

    @PostMapping("/delete/{documentId}")
    public String delete(@PathVariable Long userId, @PathVariable Long documentId,
                         RedirectAttributes redirectAttributes) {
        try {
            client.delete(userId, documentId);
            redirectAttributes.addFlashAttribute("success", "File deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete file!");
        }
        return "redirect:/users/" + userId + "/documents";
    }

}
