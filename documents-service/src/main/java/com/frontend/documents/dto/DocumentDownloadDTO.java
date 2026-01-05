package com.frontend.documents.dto;

import lombok.Data;

@Data
public class DocumentDownloadDTO {
    private byte[] fileContent;
    private String originalName;
    private String contentType;
    public DocumentDownloadDTO(byte[] fileContent, String originalName, String contentType) {
        this.fileContent = fileContent;
        this.originalName = originalName;
        this.contentType = contentType;
    }
}
