package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.ImageService;

@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor   // ← добавляем для создания конструктора с imageService
public class ImageController {

    private final ImageService imageService;   // ← убрали @Service

    @GetMapping("/{subDir}/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String subDir,
                                           @PathVariable String filename) {
        byte[] data = imageService.getImage(subDir + "/" + filename);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(data);
    }
}