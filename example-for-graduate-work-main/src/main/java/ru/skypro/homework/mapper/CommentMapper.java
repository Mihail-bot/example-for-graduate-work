package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Преобразование DTO для создания комментария в сущность.
     */
    CommentEntity toEntity(CreateOrUpdateComment dto);

    /**
     * Ручное преобразование сущности в DTO.
     * Добавляет префикс "/uploads/" к пути аватара автора,
     * чтобы фронтенд мог корректно построить URL.
     */
    default Comment toDto(CommentEntity entity) {
        if (entity == null) {
            return null;
        }
        Comment dto = new Comment();
        dto.setPk(entity.getPk());
        dto.setText(entity.getText());

        // Преобразование времени в миллисекунды (epoch)
        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli());
        }

        // Заполнение данных об авторе
        if (entity.getAuthor() != null) {
            dto.setAuthor(entity.getAuthor().getId());
            dto.setAuthorFirstName(entity.getAuthor().getFirstName());

            // Добавляем префикс /uploads/ к пути аватара
            String imagePath = entity.getAuthor().getImage();
            if (imagePath != null && !imagePath.isBlank()) {
                // Убираем возможный дублирующий слеш в начале
                dto.setAuthorImage("/uploads/" + imagePath.replaceFirst("^/", ""));
            } else {
                dto.setAuthorImage(null);
            }
        }

        return dto;
    }

    /**
     * Частичное обновление сущности из DTO.
     * Поля, переданные как null, не изменяются.
     */
    @org.mapstruct.BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget CommentEntity entity, CreateOrUpdateComment dto);

    /**
     * Преобразование списка сущностей в список DTO.
     */
    default List<Comment> toDtoList(List<CommentEntity> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}