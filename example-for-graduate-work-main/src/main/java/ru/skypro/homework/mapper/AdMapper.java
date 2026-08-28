package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AdMapper {

    // Создание сущности из DTO (автоматически)
    AdEntity toEntity(CreateOrUpdateAd dto);

    // Ручное преобразование в краткий DTO
    default Ad toDto(AdEntity entity) {
        if (entity == null) return null;
        Ad dto = new Ad();
        dto.setPk(entity.getPk());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());
        dto.setImage(entity.getImage());
        // явно устанавливаем author (id автора)
        dto.setAuthor(entity.getAuthor() != null ? entity.getAuthor().getId() : null);
        return dto;
    }

    // Ручное преобразование в расширенный DTO
    default ExtendedAd toExtendedDto(AdEntity entity) {
        if (entity == null) return null;
        ExtendedAd dto = new ExtendedAd();
        dto.setPk(entity.getPk());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());
        dto.setImage(entity.getImage());
        dto.setDescription(entity.getDescription());
        if (entity.getAuthor() != null) {
            dto.setAuthorFirstName(entity.getAuthor().getFirstName());
            dto.setAuthorLastName(entity.getAuthor().getLastName());
            dto.setEmail(entity.getAuthor().getEmail());
            dto.setPhone(entity.getAuthor().getPhone());
        }
        return dto;
    }

    // Обновление сущности (автоматически)
    void updateEntity(@MappingTarget AdEntity entity, CreateOrUpdateAd dto);

    // Преобразование списка
    default List<Ad> toDtoList(List<AdEntity> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}