package ru.skypro.homework.service;

import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdMapperService {

    // Преобразование CreateOrUpdateAd → сущность AdEntity (без автора и картинки)
    public AdEntity toEntity(CreateOrUpdateAd dto) {
        AdEntity ad = new AdEntity();
        ad.setTitle(dto.getTitle());
        ad.setPrice(dto.getPrice());
        ad.setDescription(dto.getDescription());
        // image и author устанавливаются отдельно
        return ad;
    }

    // Преобразование сущности → краткий DTO Ad (для списков)
    public Ad toDto(AdEntity entity) {
        if (entity == null) {
            return null;
        }
        Ad dto = new Ad();
        dto.setPk(entity.getPk());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());
        dto.setImage(entity.getImage());
        dto.setAuthor(entity.getAuthor() != null ? entity.getAuthor().getId() : null);
        return dto;
    }

    // Преобразование сущности → расширенный DTO ExtendedAd (для детального просмотра)
    public ExtendedAd toExtendedDto(AdEntity entity) {
        if (entity == null) {
            return null;
        }
        ExtendedAd dto = new ExtendedAd();
        dto.setPk(entity.getPk());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());
        dto.setImage(entity.getImage());
        dto.setDescription(entity.getDescription());

        UserEntity author = entity.getAuthor();
        if (author != null) {
            dto.setAuthorFirstName(author.getFirstName());
            dto.setAuthorLastName(author.getLastName());
            dto.setEmail(author.getEmail());
            dto.setPhone(author.getPhone());
        }
        return dto;
    }

    // Обновление существующей сущности из CreateOrUpdateAd (PATCH)
    public void updateEntity(CreateOrUpdateAd updateDto, AdEntity entity) {
        if (updateDto.getTitle() != null) {
            entity.setTitle(updateDto.getTitle());
        }
        if (updateDto.getPrice() != null) {
            entity.setPrice(updateDto.getPrice());
        }
        if (updateDto.getDescription() != null) {
            entity.setDescription(updateDto.getDescription());
        }
    }

    // Преобразование списка сущностей в список кратких DTO Ad
    public List<Ad> toDtoList(List<AdEntity> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}