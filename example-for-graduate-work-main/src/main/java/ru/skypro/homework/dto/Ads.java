package ru.skypro.homework.dto; //список объявлений

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Список объявлений")
public class Ads {
    @Schema(description = "Общее количество объявлений", example = "10")
    private Integer count;
    @Schema(description = "Массив объявлений")
    private List<Ad> results;
}