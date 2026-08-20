package ru.skypro.homework.dto;//список комментариев

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommentsDto {
    private Integer count = 0;
    private List<CommentDto> results = new ArrayList<>();
}