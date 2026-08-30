package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody Register register) {
        log.info("POST /register – username: {}", register.getUsername());
        boolean created = authService.register(register);
        if (created) {
            log.info("Registration successful for: {}", register.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            log.warn("Registration failed for: {}", register.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody Login login) {
        log.info("POST /login – username: {}", login.getUsername());
        boolean authenticated = authService.login(login.getUsername(), login.getPassword());
        if (authenticated) {
            log.info("Login successful for: {}", login.getUsername());
            return ResponseEntity.ok().build();
        } else {
            log.warn("Login failed for: {}", login.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}