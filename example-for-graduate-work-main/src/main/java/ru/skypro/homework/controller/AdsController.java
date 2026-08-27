package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdMapperService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdRepository adRepository;
    private final AdMapperService adMapperService;

    @Operation(summary = "Получение всех объявлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = Ads.class)))
    })
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        List<AdEntity> adEntities = adRepository.findAll();
        List<Ad> adDtos = adMapperService.toDtoList(adEntities);
        Ads response = new Ads();
        response.setCount(adDtos.size());
        response.setResults(adDtos);
        return ResponseEntity.ok(response);
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
        // Заглушка — позже добавим логику
        return ResponseEntity.status(HttpStatus.CREATED).body(new Ad());
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
        // Заглушка
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
        // Заглушка
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
        // Заглушка
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
        // Заглушка
        Ads response = new Ads();
        response.setCount(0);
        response.setResults(List.of());
        return ResponseEntity.ok(response);
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
        // Заглушка
        return ResponseEntity.ok(new byte[0]);
    }
}