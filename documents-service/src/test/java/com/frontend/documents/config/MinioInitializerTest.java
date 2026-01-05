package com.frontend.documents.config;

import io.minio.MinioClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioInitializerTest {

    @Mock
    private MinioClient minioClient;

    private MinioInitializer minioInitializer;

    @BeforeEach
    void setUp() {
        minioInitializer = new MinioInitializer(minioClient);
    }

    @Test
    void initializeMinio_SuccessfulConnection() throws Exception {
        when(minioClient.bucketExists(any())).thenReturn(true);

        minioInitializer.initializeMinio();

        verify(minioClient).listBuckets();
        verify(minioClient).bucketExists(any());
        verify(minioClient, never()).makeBucket(any());
    }

    @Test
    void initializeMinio_BucketDoesNotExist_CreatesBucket() throws Exception {
        when(minioClient.bucketExists(any())).thenReturn(false);

        minioInitializer.initializeMinio();

        verify(minioClient).listBuckets();
        verify(minioClient).bucketExists(any());
        verify(minioClient).makeBucket(any());
    }

    @Test
    void initializeMinio_ConnectionError_Continues() throws Exception {
        when(minioClient.listBuckets()).thenThrow(new RuntimeException("Connection failed"));

        minioInitializer.initializeMinio();

        verify(minioClient).listBuckets();
    }

    @Test
    void initializeMinio_BucketExistsCheckThrowsException_Continues() throws Exception {
        when(minioClient.listBuckets()).thenReturn(null);
        when(minioClient.bucketExists(any())).thenThrow(new RuntimeException("Bucket check failed"));

        minioInitializer.initializeMinio();

        verify(minioClient).listBuckets();
        verify(minioClient).bucketExists(any());
    }

    @Test
    void initializeMinio_MakeBucketThrowsException_Continues() throws Exception {
        when(minioClient.bucketExists(any())).thenReturn(false);
        doThrow(new RuntimeException("Create bucket failed")).when(minioClient).makeBucket(any());

        minioInitializer.initializeMinio();

        verify(minioClient).listBuckets();
        verify(minioClient).bucketExists(any());
        verify(minioClient).makeBucket(any());
    }

    @Test
    void constructor_InitializesCorrectly() {
        MinioInitializer initializer = new MinioInitializer(minioClient);
        assertNotNull(initializer);
    }

    @Test
    void initializeMinio_SleepsBeforeCheck() throws Exception {
        when(minioClient.bucketExists(any())).thenReturn(true);

        long startTime = System.currentTimeMillis();
        minioInitializer.initializeMinio();
        long endTime = System.currentTimeMillis();

        verify(minioClient).listBuckets();
        assertTrue(endTime - startTime >= 2900);
    }
}
