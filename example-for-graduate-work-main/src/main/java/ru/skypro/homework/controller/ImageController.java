package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.ImageService;

@Slf4j
@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{subDir}/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String subDir,
                                           @PathVariable String filename) {
        log.debug("Requested image: {}/{}", subDir, filename);
        try {
            byte[] data = imageService.getImage(subDir + "/" + filename);
            if (data == null || data.length == 0) {
                return ResponseEntity.notFound().build();
            }
            // Определяем Content-Type динамически (опционально)
            MediaType contentType = MediaType.IMAGE_JPEG; // по умолчанию
            // Можно добавить определение по расширению файла
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(data);
        } catch (Exception e) {
            log.error("Error retrieving image: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}