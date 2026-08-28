package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)   // включает аннотации @PreAuthorize
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;   // наш CustomUserDetailsService

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF для REST API (иначе POST/PUT/PATCH будут требовать токен)
                .csrf().disable()

                // Управление сессиями: stateless (не храним сессии на сервере) или по умолчанию (SessionCreationPolicy.IF_REQUIRED)
                // Для REST с Basic-аутентификацией лучше оставить IF_REQUIRED, чтобы не создавать сессии лишний раз
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()

                // Настройка правил авторизации
                .authorizeHttpRequests(auth -> auth
                        // Публичные эндпоинты (доступны без авторизации)
                        .mvcMatchers("/register", "/login").permitAll()
                        .mvcMatchers("/ads", "/ads/*").permitAll()           // просмотр всех объявлений и деталей (согласно спецификации)
                        .mvcMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll() // для Swagger

                        // Эндпоинты, требующие аутентификации (любой роли)
                        .mvcMatchers("/users/**", "/ads/me", "/ads/*/comments/**").authenticated()

                        // Эндпоинты только для ADMIN (пример)
                        .mvcMatchers("/admin/**").hasRole("ADMIN")

                        // Все остальные запросы – аутентифицированы
                        .anyRequest().authenticated()
                )

                // Используем HTTP Basic (для тестирования через Postman)
                // Если хотите форму логина — замените на .formLogin()
                .httpBasic()
                .and()

                // Подключаем наш UserDetailsService для загрузки пользователей из БД
                .userDetailsService(userDetailsService);

        return http.build();
    }

    // Бин для аутентификации (нужен для ручного вызова в контроллерах, если потребуется)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Бин кодировщика паролей (используем BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}