package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentMapperService {

    // Преобразование CreateOrUpdateComment → сущность CommentEntity (без автора и ad)
    public CommentEntity toEntity(CreateOrUpdateComment dto) {
        CommentEntity comment = new CommentEntity();
        comment.setText(dto.getText());
        // createdAt устанавливается автоматически @CreationTimestamp
        return comment;
    }

    // Преобразование сущности → DTO Comment
    public Comment toDto(CommentEntity entity) {
        if (entity == null) {
            return null;
        }
        Comment dto = new Comment();
        dto.setPk(entity.getPk());
        dto.setText(entity.getText());

        // Конвертация LocalDateTime → миллисекунды (epoch)
        LocalDateTime createdAt = entity.getCreatedAt();
        if (createdAt != null) {
            long epochMillis = createdAt.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
            dto.setCreatedAt(epochMillis);
        } else {
            dto.setCreatedAt(null);
        }

        UserEntity author = entity.getAuthor();
        if (author != null) {
            dto.setAuthor(author.getId());
            dto.setAuthorFirstName(author.getFirstName());
            dto.setAuthorImage(author.getImage());
        }
        return dto;
    }

    // Обновление существующей сущности из CreateOrUpdateComment (PATCH)
    public void updateEntity(CreateOrUpdateComment updateDto, CommentEntity entity) {
        if (updateDto.getText() != null) {
            entity.setText(updateDto.getText());
        }
    }

    // Преобразование списка сущностей в список DTO Comment
    public List<Comment> toDtoList(List<CommentEntity> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}