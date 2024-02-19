package com.example.demo.controller;

import com.example.demo.model.ETagDTO;
import com.example.demo.service.MinIoService;
import io.minio.messages.Bucket;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/minio")
@RequiredArgsConstructor

public class MinIoController {
    private final MinIoService minIoService;

    @PostMapping("/create-bucket")
    public ResponseEntity<Bucket> createBucket(@RequestParam String bucketName){
        Bucket bucket = minIoService.createBucket(bucketName);
        return ResponseEntity.ok(bucket);
    }

    @PostMapping("/upload")
    public ResponseEntity<String>uploadFile(@RequestParam String bucketName, @RequestParam MultipartFile file){
        return minIoService.uploadFile(bucketName, file);
    }

    @GetMapping("/download")
    public ResponseEntity<File> downloadFile(@RequestParam String bucketName,
                             @RequestParam String fileName,
                             HttpServletResponse response) {
        minIoService.downloadFile(bucketName, fileName, response);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/view-by-etag")
    public void getFileByETag(@RequestBody ETagDTO eTagDTO, HttpServletResponse response){
        minIoService.viewByETag(eTagDTO.getETag(), eTagDTO.getBucketName(), eTagDTO.getFileName(), response);
    }

}