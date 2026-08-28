package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "Расширенная информация об объявлении")
public class ExtendedAd {
    @Schema(description = "ID объявления", example = "100")
    private Integer pk;
    @Schema(description = "Имя автора", example = "John")
    private String authorFirstName;
    @Schema(description = "Фамилия автора", example = "Doe")
    private String authorLastName;
    @Schema(description = "Описание", example = "Отличный ноутбук в хорошем состоянии")
    private String description;
    @Schema(description = "Логин автора", example = "john.doe@example.com")
    private String email;
    @Schema(description = "Ссылка на картинку", example = "/images/ad123.jpg")
    private String image;
    @Schema(description = "Телефон автора", example = "+7 999 123-45-67")
    private String phone;
    @Schema(description = "Цена", example = "50000")
    private Integer price;
    @Schema(description = "Заголовок", example = "Продам ноутбук")
    private String title;
}