package com.example.demo.service.impl;

import com.example.demo.service.MinIoService;
import io.minio.*;
import io.minio.messages.Bucket;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import static sun.font.CreatedFontTracker.MAX_FILE_SIZE;

@RequiredArgsConstructor
@Service
public class MinIoServiceImpl implements MinIoService {
    private final MinioClient minioClient;
    private final ConvertorServiceImpl convertor;


    @Override
    public Bucket createBucket(String bucketName) {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            Iterable<Bucket> buckets = minioClient.listBuckets();
            for (Bucket bucket : buckets) {
                if (bucket.name().equals(bucketName)) {
                    return bucket;
                }
            }
            throw new RuntimeException("Bucket not found after creation: " + bucketName);
        } catch (Exception e) {
            throw new RuntimeException("Error creating bucket: " + e.getMessage(), e);
        }
    }


    @Override
    public ResponseEntity<String> uploadFile(String bucketName, MultipartFile file) {
        try {
            // Проверка наличия bucketName и файла
            if (bucketName == null || file == null) {
                throw new IllegalArgumentException("Bucket name and file cannot be null.");
            }

            // Проверка размера файла, если это необходимо
            long fileSize = file.getSize();
            if (fileSize > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File size exceeds the maximum allowed size.");
            }

            String originalFileName = file.getOriginalFilename();
            byte[] fileBytes = file.getBytes();
            String objectName = UUID.randomUUID() + "-" + originalFileName;

            // Проверка наличия клиента MinIO
            if (minioClient == null) {
                throw new RuntimeException("MinIO client is null.");
            }

            // Загрузка объекта в бакет MinIO
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(new ByteArrayInputStream(fileBytes), fileBytes.length, -1)
                    .build());

            // Получение URL загруженного файла
            String fileUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());

            return ResponseEntity.ok("File uploaded successfully.\nURL: " + fileUrl);
        } catch (IllegalArgumentException e) {
            // Перехватывать исключение IllegalArgumentException и возвращать ошибку клиенту
            return ResponseEntity.badRequest().body("Error uploading file: " + e.getMessage());
        } catch (Exception e) {
            // Перехватывать остальные исключения и возвращать ошибку сервера клиенту
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }

    @Override
    public void downloadFile(String bucketName, String fileName, HttpServletResponse response) {
        if (bucketName == null || bucketName.isEmpty() || fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Required parameters 'bucketName' and 'fileName' are not present.");
        }
        try {
            InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            FileCopyUtils.copy(stream, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file: " + e.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public void viewByETag(String etag, String bucketName, String fileName, HttpServletResponse response) {
        InputStream file = minioClient.getObject(
                GetObjectArgs.builder()
                        .matchETag(etag)
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );

        response.setContentType("application/pdf");

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = file.read(buffer)) != -1) {
            response.getOutputStream().write(buffer, 0, bytesRead);
        }

        response.flushBuffer();
        file.close();
    }
}