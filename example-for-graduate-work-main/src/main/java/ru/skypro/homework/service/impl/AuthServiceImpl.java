package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserMapperService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapperService userMapperService;

    @Override
    public boolean login(String userName, String password) {
        return userRepository.findByEmail(userName)
                .map(user -> encoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    @Override
    public boolean register(Register register) {
        if (userRepository.existsByEmail(register.getUsername())) {
            return false;
        }
        UserEntity user = userMapperService.toEntity(register);
        user.setPassword(encoder.encode(register.getPassword()));
        userRepository.save(user);
        return true;
    }
}
