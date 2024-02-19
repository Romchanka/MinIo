package com.example.demo.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIoClient {

    private static final String MINIO_URL = "http://localhost:9000";
    private static final String ACCESS_KEY = "Fwf1ROYWCE3jgeiPXsCH";
    private static final String SECRET_KEY = "eJwjDQQzsWjfzKeNdUULsdyevKKnigqa9ENoHwMe";

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(MINIO_URL)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
    }
}