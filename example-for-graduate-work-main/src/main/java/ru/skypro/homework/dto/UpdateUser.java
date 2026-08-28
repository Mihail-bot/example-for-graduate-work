package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "Данные для обновления пользователя")
public class UpdateUser {
    @Schema(description = "Имя", example = "Jonathan", minLength = 3, maxLength = 10)
    @Size(min = 3, max = 10)
    private String firstName;

    @Schema(description = "Фамилия", example = "Smith", minLength = 3, maxLength = 10)
    @Size(min = 3, max = 10)
    private String lastName;

    @Schema(description = "Телефон", example = "+7 999 987-65-43",
            pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
}