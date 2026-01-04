package com.frontend.documents.service;

import com.frontend.documents.dto.DocumentDownloadDTO;
import com.frontend.documents.dto.DocumentResponseDTO;
import com.frontend.documents.model.Document;
import com.frontend.documents.repository.DocumentRepository;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository repository;
    private final MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    public DocumentService(DocumentRepository repository, MinioClient minioClient) {
        this.repository = repository;
        this.minioClient = minioClient;
    }
    
    public void testMinioConnection() {
        try {
            // Просто провери дали може да изброи buckets
            minioClient.listBuckets();
            System.out.println("✅ MinIO connection test passed");
        } catch (Exception e) {
            throw new RuntimeException("MinIO connection test failed: " + e.getMessage(), e);
        }
    }

    //    1. Конструктор → 2. @Value инжектиране → 3. @PostConstruct we want the bucket to be ready
//    извиква след като Spring инжектира всички зависимости
    public void initBucket() {
        try {
            Thread.sleep(5000);

            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DocumentResponseDTO uploadDocument(Long userId, MultipartFile toUpload) {
        try {
            System.out.println("🚀 Starting upload to MinIO...");

            // 1. ПРОВЕРИ ДАЛИ BUCKET СЪЩЕСТВУВА
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );

            if (!bucketExists) {
                System.out.println("📦 Creating bucket: " + bucketName);
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
            }

            // 2. КАЧИ ФАЙЛА
            String objectName = getMinioName(userId, toUpload.getOriginalFilename());
            System.out.println("📤 Uploading file: " + objectName);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .contentType(toUpload.getContentType())
                            .stream(toUpload.getInputStream(), toUpload.getSize(), -1)
                            .build()
            );

            System.out.println("✅ File uploaded to MinIO: " + objectName);

            // 3. ЗАПАЗИ В БАЗАТА
            Document toSave = new Document();
            toSave.setUserId(userId);
            toSave.setObjectName(objectName);
            toSave.setOriginalFilename(toUpload.getOriginalFilename());
            toSave.setFileSize(toUpload.getSize());
            toSave.setContentType(toUpload.getContentType());

            Document saved = repository.save(toSave);
            System.out.println("💾 Document saved to DB: " + saved.getId());

            return toDTO(saved);

        } catch (Exception e) {
            System.err.println("❌ UPLOAD FAILED: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Upload failed: " + e.getMessage(), e);
        }
    }
//    public DocumentResponseDTO uploadDocument(Long userId, MultipartFile toUpload) {
//
//        try {
//            String objectName = getMinioName(userId, toUpload.getOriginalFilename());
//
//            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).contentType(toUpload.getContentType()).stream(toUpload.getInputStream(), toUpload.getSize(), -1).build());
//
//            Document toSave = new Document();
//            toSave.setUserId(userId);
//            toSave.setObjectName(objectName);
//            toSave.setOriginalFilename(toUpload.getOriginalFilename());
//            toSave.setFileSize(toUpload.getSize());
//            toSave.setContentType(toUpload.getContentType());
//
//            Document saved = repository.save(toSave);
//            return toDTO(saved);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public DocumentDownloadDTO downloadDocument(Long userId, Long documentId) {

        Document found = repository.findByUserIdAndId(userId, documentId).orElseThrow(() -> new RuntimeException("Such document does not exist!"));
        try {
            InputStream filecontent = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(found.getObjectName()).build());

            try (filecontent) {
                byte[] content = filecontent.readAllBytes();
                return new DocumentDownloadDTO(content, found.getOriginalFilename(), found.getContentType());
            }
        } catch (Exception e) {
            throw new RuntimeException("Downloading document failed!");
        }
    }

    public void deleteDocument(Long userId, Long documentId) {
        try {
            Document found = repository.findByUserIdAndId(userId, documentId).orElseThrow(() -> new RuntimeException("Such document does not exist!"));

            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(found.getObjectName()).build());

            repository.delete(found);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DocumentResponseDTO findById(Long userId, Long id) {
        Document found = repository.findByUserIdAndId(userId, id).orElseThrow(() -> new RuntimeException("Document not found!"));
        return toDTO(found);
    }

    public List<DocumentResponseDTO> getAllDocumentsByUserID(Long userId) {
        return repository.findAllByUserId(userId).stream().map(this::toDTO).toList();
    }

    private DocumentResponseDTO toDTO(Document document) {
        return new DocumentResponseDTO(document.getId(), document.getUserId(), document.getOriginalFilename(), document.getFileSize(), document.getContentType(), document.getUploadTime());
    }

    private String getMinioName(Long userId, String originalFilename) {
        String uuid = java.util.UUID.randomUUID().toString();
        String cleanName = originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
        return userId + "/" + uuid + "_" + cleanName;
    }
}
