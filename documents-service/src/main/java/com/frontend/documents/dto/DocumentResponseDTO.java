package com.frontend.documents.dto;

import java.time.LocalDateTime;

public record DocumentResponseDTO(Long id, Long userId, String originalFilename,
                                  Long fileSize, String contentType, LocalDateTime uploadTime) {
}
