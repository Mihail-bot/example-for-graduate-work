package ru.skypro.homework.dto; //список объявлений

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class AdsDto {
    private Integer count = 0;
    private List<AdDto> results = new ArrayList<>();
}
