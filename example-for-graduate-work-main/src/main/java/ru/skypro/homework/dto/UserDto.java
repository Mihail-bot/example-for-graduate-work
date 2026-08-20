package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class UserDto {
    private Integer id = 0;
    private String email = "";
    private String firstName = "";
    private String lastName = "";
    private String phone = "";
    private String role = "USER";
    private String image = "";
}
