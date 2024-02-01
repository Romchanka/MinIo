package com.example.demo.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinIoClientUpload {
    private static final String MINIO_URL = "http://localhost:9000";
    private static final String ACCESS_KEY = "Fwf1ROYWCE3jgeiPXsCH";
    private static final String SECRET_KEY = "eJwjDQQzsWjfzKeNdUULsdyevKKnigqa9ENoHwMe";
    private static final String BUCKET_NAME = "clientid";

    public static void main(String[] args) {
        try {
            // Создаем клиент Minio
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(MINIO_URL)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .build();

            // Проверяем, существует ли бакет, и создаем его, если нет
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            }

            // Загружаем файл в бакет. LocalFilePath путь к файлу. remoteFilePath название файла
            uploadFile(minioClient, "C:\\Users\\Futur\\Desktop\\java\\img\\sonata.jpeg", "idCLient1");

        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private static void uploadFile(MinioClient minioClient, String localFilePath, String remoteFilePath)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException, ErrorResponseException,
            InternalException, InsufficientDataException, InvalidResponseException,
            XmlParserException, ServerException {
        // Проверяем, что localFilePath указывает на существующий файл
        File file = new File(localFilePath);
        if (!file.isFile()) {
            System.out.println("check format and exist file: wrong format please check format carefully " + localFilePath);
            return;
        }

        // Преобразуем URL в имя объекта без запрещенных символов
        String objectName = remoteFilePath.replaceAll("[^a-zA-Z0-9._-]", "_");

        // Создаем объект UploadObjectArgs
        UploadObjectArgs args = UploadObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(objectName)
                .filename(localFilePath)
                .build();

        // Загружаем файл
        minioClient.uploadObject(args);
        System.out.println("success");
    }
}