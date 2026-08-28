package ru.skypro.homework.dto;//список комментариев

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor   // ← обязательно
@AllArgsConstructor
@Schema(description = "Список комментариев")
public class Comments {
    @Schema(description = "Общее количество комментариев", example = "5")
    private Integer count;
    @Schema(description = "Массив комментариев")
    private List<Comment> results;
}