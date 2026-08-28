package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Получить профиль текущего пользователя.
     * Используем userMapper.toDto() для преобразования сущности в DTO.
     */
    public User getCurrentUser() {
        String email = getCurrentEmail();
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toDto(entity);
    }

    /**
     * Обновить профиль текущего пользователя.
     * Используем userMapper.updateEntity() для частичного обновления.
     * Затем сохраняем и преобразуем в DTO ответа.
     */
    @Transactional
    public UpdateUser updateCurrentUser(UpdateUser updateDto) {
        String email = getCurrentEmail();
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        // Маппер обновляет только переданные поля (не null)
        userMapper.updateEntity(entity, updateDto);
        UserEntity updated = userRepository.save(entity);
        // Возвращаем обновлённые данные в формате DTO
        return userMapper.toUpdateDto(updated);
    }

    /**
     * Смена пароля. Маппер здесь не используется, так как пароль не маппится.
     */
    @Transactional
    public void changePassword(NewPassword passwordDto) {
        String email = getCurrentEmail();
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), entity.getPassword())) {
            throw new ForbiddenException("Current password is incorrect");
        }
        entity.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(entity);
    }

    /**
     * Обновить аватар (устанавливаем путь к файлу).
     * Маппер не нужен, так как меняется одно поле.
     */
    @Transactional
    public String updateAvatar(String imagePath) {
        String email = getCurrentEmail();
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        entity.setImage(imagePath);
        userRepository.save(entity);
        return imagePath;
    }

    private String getCurrentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }
        return auth.getName();
    }
}