package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;

    /**
     * Получить все объявления.
     * Используем adMapper.toDtoList() для преобразования списка.
     */
    public Ads getAllAds() {
        List<AdEntity> entities = adRepository.findAll();
        List<Ad> dtos = adMapper.toDtoList(entities);
        return Ads.builder()
                .count(dtos.size())
                .results(dtos)
                .build();
    }

    /**
     * Получить объявление по ID.
     * Используем adMapper.toExtendedDto() – расширенный ответ.
     */
    public ExtendedAd getAdById(Integer id) {
        AdEntity entity = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ad not found"));
        return adMapper.toExtendedDto(entity);
    }

    /**
     * Создать объявление.
     * Сначала преобразуем DTO → сущность (без автора и изображения).
     * Затем вручную устанавливаем автора и изображение, сохраняем.
     * В конце преобразуем обратно в краткий DTO.
     */
    @Transactional
    public Ad createAd(CreateOrUpdateAd createDto, MultipartFile image) {
        String email = getCurrentEmail();
        UserEntity author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Маппер создаёт новую сущность из DTO
        AdEntity ad = adMapper.toEntity(createDto);
        ad.setAuthor(author);   // устанавливаем связь
        if (image != null && !image.isEmpty()) {
            String imagePath = imageService.saveImage(image, "ads");
            ad.setImage(imagePath);
        }
        AdEntity saved = adRepository.save(ad);
        return adMapper.toDto(saved);
    }

    /**
     * Обновить объявление.
     * Получаем сущность, проверяем права, обновляем через маппер, сохраняем.
     */
    @Transactional
    public Ad updateAd(Integer id, CreateOrUpdateAd updateDto) {
        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ad not found"));
        checkOwnershipOrAdmin(ad.getAuthor().getEmail());
        // маппер обновляет только переданные поля
        adMapper.updateEntity(ad, updateDto);
        AdEntity updated = adRepository.save(ad);
        return adMapper.toDto(updated);
    }

    /**
     * Удалить объявление.
     * Маппер не нужен.
     */
    @Transactional
    public void deleteAd(Integer id) {
        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ad not found"));
        checkOwnershipOrAdmin(ad.getAuthor().getEmail());
        adRepository.delete(ad);
    }

    /**
     * Получить объявления текущего пользователя.
     * Используем adMapper.toDtoList().
     */
    public Ads getMyAds() {
        String email = getCurrentEmail();
        UserEntity author = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<AdEntity> entities = adRepository.findByAuthorId(author.getId());
        List<Ad> dtos = adMapper.toDtoList(entities);
        return new Ads(dtos.size(), dtos);
    }

    /**
     * Обновить картинку объявления (только изображение).
     */
    @Transactional
    public void updateAdImage(Integer adId, MultipartFile image) {
        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Ad not found"));
        checkOwnershipOrAdmin(ad.getAuthor().getEmail());
        if (image != null && !image.isEmpty()) {
            String newImagePath = imageService.saveImage(image, "ads");
            ad.setImage(newImagePath);
            adRepository.save(ad);
        }
    }

    /**
     * Получить картинку объявления (байты).
     */
    public byte[] getAdImage(Integer adId) {
        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Ad not found"));
        if (ad.getImage() == null) {
            return new byte[0];
        }
        return imageService.getImage(ad.getImage());
    }

    // === вспомогательные методы ===
    private void checkOwnershipOrAdmin(String ownerEmail) {
        String currentEmail = getCurrentEmail();
        if (currentEmail.equals(ownerEmail)) return;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(granted -> granted.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new ForbiddenException("Access denied");
        }
    }

    private String getCurrentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }
        return auth.getName();
    }
}