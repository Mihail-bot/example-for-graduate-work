package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Информация о комментарии")
public class Comment {
    @Schema(description = "ID автора комментария", example = "5")
    private Integer author;
    @Schema(description = "Ссылка на аватар автора", example = "/avatars/user5.jpg")
    private String authorImage;
    @Schema(description = "Имя автора", example = "Jane")
    private String authorFirstName;
    @Schema(description = "Дата создания в миллисекундах", example = "1678901234567")
    private Long createdAt;
    @Schema(description = "ID комментария", example = "42")
    private Integer pk;
    @Schema(description = "Текст комментария", example = "Отличное объявление!")
    private String text;
}