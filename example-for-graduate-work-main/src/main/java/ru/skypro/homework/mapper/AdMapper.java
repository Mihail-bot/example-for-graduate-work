package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AdMapper {

    // Создание сущности из DTO
    AdEntity toEntity(CreateOrUpdateAd dto);

    // Ручное преобразование в краткий DTO (избегаем @Mapping проблем)
    default Ad toDto(AdEntity entity) {
        if (entity == null) return null;
        Ad dto = new Ad();
        dto.setPk(entity.getPk());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());
        dto.setImage(entity.getImage());
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

    // Обновление сущности – игнорируем null поля (защита от затирания)
    @org.mapstruct.BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget AdEntity entity, CreateOrUpdateAd dto);

    // Преобразование списка (можно оставить автоматическое, но проще через default)
    default List<Ad> toDtoList(List<AdEntity> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}