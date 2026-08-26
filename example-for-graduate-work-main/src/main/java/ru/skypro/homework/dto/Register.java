package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.*;

@Data
@Schema(description = "Регистрационные данные")
public class Register {
    @Schema(description = "Логин пользователя", example = "john_doe", required = true, minLength = 4, maxLength = 32)
    @NotBlank @Size(min = 4, max = 32)
    private String username;

    @Schema(description = "Пароль", example = "Passw0rd!", required = true, minLength = 8, maxLength = 16)
    @NotBlank @Size(min = 8, max = 16)
    private String password;

    @Schema(description = "Имя пользователя", example = "John", required = true, minLength = 2, maxLength = 16)
    @NotBlank @Size(min = 2, max = 16)
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Doe", required = true, minLength = 2, maxLength = 16)
    @NotBlank @Size(min = 2, max = 16)
    private String lastName;

    @Schema(description = "Телефон пользователя в формате +7 (XXX) XXX-XX-XX",
            example = "+7 999 123-45-67", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;

    @Schema(description = "Роль пользователя", allowableValues = {"USER", "ADMIN"}, example = "USER")
    private String role;
}