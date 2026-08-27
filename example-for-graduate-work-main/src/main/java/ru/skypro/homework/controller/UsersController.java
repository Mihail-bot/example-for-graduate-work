package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserMapperService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserRepository userRepository;
    private final UserMapperService userMapperService;

    @Operation(summary = "Обновление пароля", description = "Смена текущего пароля на новый")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пароль обновлён"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@Valid @RequestBody NewPassword newPassword) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/me")
    public ResponseEntity<User> getUser() {
        return ResponseEntity.ok(new User());
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновлено",
                    content = @Content(schema = @Schema(implementation = UpdateUser.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@Valid @RequestBody UpdateUser updateUser) {
        return ResponseEntity.ok(updateUser);
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аватар обновлён"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @PatchMapping(value = "/me/image", consumes = "multipart/form-data")
    public ResponseEntity<Void> updateUserImage(@RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok().build();
    }
}