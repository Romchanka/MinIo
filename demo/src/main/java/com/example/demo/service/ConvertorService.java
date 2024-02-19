package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface ConvertorService {
    byte[] imageToPDF(MultipartFile file);
}
