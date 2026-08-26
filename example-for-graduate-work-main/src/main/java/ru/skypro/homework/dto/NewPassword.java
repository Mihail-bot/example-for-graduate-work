package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "Данные для смены пароля")
public class NewPassword {
    @Schema(description = "Текущий пароль", example = "oldPass123", required = true, minLength = 8, maxLength = 16)
    @NotBlank @Size(min = 8, max = 16)
    private String currentPassword;

    @Schema(description = "Новый пароль", example = "newPass456", required = true, minLength = 8, maxLength = 16)
    @NotBlank @Size(min = 8, max = 16)
    private String newPassword;
}