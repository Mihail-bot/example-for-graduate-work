package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "Краткая информация об объявлении")
public class Ad {
    @Schema(description = "ID автора объявления", example = "1")
    private Integer author;
    @Schema(description = "Ссылка на картинку", example = "/images/ad123.jpg")
    private String image;
    @Schema(description = "ID объявления", example = "100")
    private Integer pk;
    @Schema(description = "Цена", example = "50000")
    private Integer price;
    @Schema(description = "Заголовок", example = "Продам ноутбук")
    private String title;
}