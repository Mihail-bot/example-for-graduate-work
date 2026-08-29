package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public boolean login(String userName, String password) {
        log.debug("Login attempt for user: {}", userName);
        return userRepository.findByEmail(userName)
                .map(user -> {
                    boolean matches = passwordEncoder.matches(password, user.getPassword());
                    log.debug("Password match: {}", matches);
                    return matches;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean register(Register register) {
        log.info("Register request for username: {}", register.getUsername());
        if (userRepository.existsByEmail(register.getUsername())) {
            log.warn("User already exists: {}", register.getUsername());
            return false;
        }
        UserEntity user = userMapper.toEntity(register);
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        UserEntity saved = userRepository.save(user);
        log.info("User saved with id: {}", saved.getId());
        return true;
    }
}