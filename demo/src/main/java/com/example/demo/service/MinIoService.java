package com.example.demo.service;

import io.minio.messages.Bucket;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface MinIoService {

    Bucket createBucket(String bucketName);

    ResponseEntity<String> uploadFile(String bucketName, MultipartFile file);

    void downloadFile(String bucketName, String fileName, HttpServletResponse response);

    void viewByETag(String etag, String bucketName, String filaName, HttpServletResponse response);


}

