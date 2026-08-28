package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentEntity toEntity(CreateOrUpdateComment dto);

    default Comment toDto(CommentEntity entity) {
        if (entity == null) return null;
        Comment dto = new Comment();
        dto.setPk(entity.getPk());
        dto.setText(entity.getText());
        // преобразование времени в миллисекунды
        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli());
        }
        // поля автора
        if (entity.getAuthor() != null) {
            dto.setAuthor(entity.getAuthor().getId());
            dto.setAuthorFirstName(entity.getAuthor().getFirstName());
            dto.setAuthorImage(entity.getAuthor().getImage());
        }
        return dto;
    }

    void updateEntity(@MappingTarget CommentEntity entity, CreateOrUpdateComment dto);

    default List<Comment> toDtoList(List<CommentEntity> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
