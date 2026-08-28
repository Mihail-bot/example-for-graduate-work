package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    /**
     * Сохранить изображение и вернуть путь для хранения в БД.
     */
    public String saveImage(MultipartFile file, String subDir) {
        try {
            Path dir = Paths.get(uploadDir, subDir);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetPath = dir.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            // возвращаем относительный путь, который будем хранить в БД
            return subDir + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    /**
     * Получить изображение в виде байтов.
     */
    public byte[] getImage(String relativePath) {
        try {
            Path fullPath = Paths.get(uploadDir, relativePath);
            if (!Files.exists(fullPath)) {
                return new byte[0];
            }
            return Files.readAllBytes(fullPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image", e);
        }
    }

    /**
     * Удалить изображение (опционально).
     */
    public void deleteImage(String relativePath) {
        try {
            Path fullPath = Paths.get(uploadDir, relativePath);
            Files.deleteIfExists(fullPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }
}