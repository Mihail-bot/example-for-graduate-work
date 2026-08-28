package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "Информация о пользователе")
public class User {
    @Schema(description = "ID пользователя", example = "1")
    private Integer id;
    @Schema(description = "Логин (email)", example = "john.doe@example.com")
    private String email;
    @Schema(description = "Имя", example = "John")
    private String firstName;
    @Schema(description = "Фамилия", example = "Doe")
    private String lastName;
    @Schema(description = "Телефон", example = "+7 999 123-45-67")
    private String phone;
    @Schema(description = "Роль", allowableValues = {"USER", "ADMIN"}, example = "USER")
    private String role;
    @Schema(description = "Ссылка на аватар", example = "/avatars/user1.jpg")
    private String image;
}