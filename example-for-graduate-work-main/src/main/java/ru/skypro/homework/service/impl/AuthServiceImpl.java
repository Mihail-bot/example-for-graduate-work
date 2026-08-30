package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;  // ← добавить импорт
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

@Slf4j  // ← добавить аннотацию
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public boolean login(String userName, String password) {
        log.info("=== LOGIN ATTEMPT ===");
        log.info("Username: {}", userName);

        // 1. Ищем пользователя в БД
        Optional<UserEntity> userOpt = userRepository.findByEmail(userName);
        if (userOpt.isEmpty()) {
            log.warn("User NOT found: {}", userName);
            return false;
        }

        UserEntity user = userOpt.get();
        log.info("User found: id={}, email={}, firstName={}, lastName={}",
                user.getId(), user.getEmail(), user.getFirstName(), user.getLastName());

        // 2. Проверяем пароль
        String storedHash = user.getPassword();
        log.info("Stored password hash: {}", storedHash);
        log.info("Password to check: {}", password); // только для отладки (удалить в продакшене)

        boolean matches = passwordEncoder.matches(password, storedHash);
        log.info("Password match result: {}", matches);

        if (matches) {
            log.info("Login SUCCESS for user: {}", userName);
        } else {
            log.warn("Login FAILED for user: {} – password does not match", userName);
        }

        return matches;
    }

    @Override
    @Transactional
    public boolean register(Register register) {
        log.info("Register request: {}", register.getUsername());
        if (userRepository.existsByEmail(register.getUsername())) {
            log.warn("User already exists: {}", register.getUsername());
            return false;
        }
        UserEntity user = userMapper.toEntity(register);
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        UserEntity saved = userRepository.save(user);
        log.info("User registered with id: {}", saved.getId());
        return true;
    }
}