package ru.skypro.homework.controller;

import ru.skypro.homework.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/ads")
public class AdsController {

    @Operation(summary = "Получение всех объявлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = Ads.class)))
    })
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        Ads ads = new Ads();
        ads.setCount(0);
        ads.setResults(java.util.Collections.emptyList());
        return ResponseEntity.ok(ads);
    }

    @Operation(summary = "Добавление объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Создано",
                    content = @Content(schema = @Schema(implementation = Ad.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Ad> addAd(
            @RequestPart("properties") @Valid CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image) {
        // обратите внимание: поле image обязательно в спецификации (required: true)
        return ResponseEntity.status(201).body(new Ad());
    }

    @Operation(summary = "Получение информации об объявлении по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = ExtendedAd.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {
        return ResponseEntity.ok(new ExtendedAd());
    }

    @Operation(summary = "Удаление объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Удалено"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Integer id) {
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновлено",
                    content = @Content(schema = @Schema(implementation = Ad.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable Integer id,
                                        @Valid @RequestBody CreateOrUpdateAd updateAd) {
        return ResponseEntity.ok(new Ad());
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = Ads.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe() {
        Ads ads = new Ads();
        ads.setCount(0);
        ads.setResults(java.util.Collections.emptyList());
        return ResponseEntity.ok(ads);
    }

    @Operation(summary = "Обновление картинки объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновлено",
                    content = @Content(schema = @Schema(type = "array", format = "byte"))),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id,
                                              @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(new byte[0]);
    }
}