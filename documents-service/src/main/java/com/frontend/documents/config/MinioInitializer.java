package com.frontend.documents.config;

import io.minio.MinioClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MinioInitializer {

    private final MinioClient minioClient;
    private static final int TIME_TO_SLEEP = 3000;

    public MinioInitializer(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeMinio() {
        try {
            System.out.println("Testing MinIO connection...");
            Thread.sleep(TIME_TO_SLEEP);

            minioClient.listBuckets();
            System.out.println("MinIO connection successful!");

            boolean bucketExists = minioClient
                    .bucketExists(io.minio.BucketExistsArgs.builder().bucket("user-documents").build());

            if (!bucketExists) {
                System.out.println("Creating bucket: user-documents");
                minioClient.makeBucket(io.minio.MakeBucketArgs.builder().bucket("user-documents").build());
            }

        } catch (Exception e) {
            System.err.println("MinIO connection issue: " + e.getMessage());
            System.err.println("But application continues to work!");
        }
    }
}