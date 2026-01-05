package com.frontend.documents.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String originalFilename;
    private String objectName; // unique name in minio

    private Long fileSize;
    private String contentType; // txt png ..
    private LocalDateTime uploadTime;

    @PrePersist
    protected void beforeSave() { // по конвенция се прави protected
        uploadTime = LocalDateTime.now();
    }
}
