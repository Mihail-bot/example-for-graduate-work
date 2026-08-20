package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class AdDto {
    private Integer author = 0;
    private String image = "";
    private Integer pk = 0;
    private Integer price = 0;
    private String title = "";
}