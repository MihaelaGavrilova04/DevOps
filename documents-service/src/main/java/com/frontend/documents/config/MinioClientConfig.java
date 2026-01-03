package com.frontend.documents.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfig {

    @Bean
    public MinioClient getMinioClient(){
        return MinioClient.builder()
                .endpoint("http://minio:9000")
                .credentials("minioadmin", "minioadmin")
                .build();
    }
}
