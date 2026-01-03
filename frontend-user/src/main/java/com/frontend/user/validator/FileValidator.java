package com.frontend.user.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;


@Component
public class FileValidator {
    private static final int MB_IN_BYTES = 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "doc", "docx", "txt", "jpg", "png", "jpeg");

    public void validateFileContent(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File passed is not a valid file!");
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            throw new IllegalArgumentException("File passed is empty!");
        }

        if (file.getSize() > 10 * MB_IN_BYTES) {
            throw new IllegalArgumentException("File passed is too large, must be max 10 MB!");
        }

        validateExtension(file.getOriginalFilename());
    }

    private void validateExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("File has no extension!");
        }

        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Unsupported file format: " + extension);
        }
    }

}
