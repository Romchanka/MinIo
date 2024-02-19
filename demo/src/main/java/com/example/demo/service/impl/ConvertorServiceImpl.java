package com.example.demo.service.impl;

import com.example.demo.service.ConvertorService;
import com.zaxxer.hikari.pool.HikariProxyCallableStatement;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ConvertorServiceImpl implements ConvertorService {
    @SneakyThrows
    @Override
        public byte[] imageToPDF(MultipartFile file) {
            String originalFilename = file.getOriginalFilename();
        HikariProxyCallableStatement image;
        if (originalFilename != null && (originalFilename.endsWith(".txt") || originalFilename.endsWith(".doc"))) {
                // Пропускаем конвертацию для файлов .txt и .doc
                return file.getBytes();
            }

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
                    if (bufferedImage != null) {
                        PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, file.getBytes(), null);
                        float width = bufferedImage.getWidth();
                        float height = bufferedImage.getHeight();
                        float scale = Math.min(1, Math.min(595 / width, 842 / height)); // A4 page size in points (72 points per inch)
                        contentStream.drawImage(pdImage, 0, 0, width * scale, height * scale);
                    }
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                document.save(outputStream);
                return outputStream.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при конвертации изображения в PDF: " + e.getMessage());
            }
        }
    }
