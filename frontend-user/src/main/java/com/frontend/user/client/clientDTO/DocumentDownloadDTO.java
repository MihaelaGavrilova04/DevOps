package com.frontend.user.client.clientDTO;

import lombok.Data;

@Data
public class DocumentDownloadDTO{
    public DocumentDownloadDTO(byte[] fileContent, String originalName, String contentType){
        this.fileContent = fileContent;
        this.originalName = originalName;
        this.contentType = contentType;
    }
    private byte[] fileContent;
    private String originalName;
    private String contentType;
}
