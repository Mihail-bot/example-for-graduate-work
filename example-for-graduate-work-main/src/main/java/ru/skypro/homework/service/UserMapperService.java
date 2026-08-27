package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapperService {

    public UserEntity toEntity(Register register) {
        UserEntity user = new UserEntity();
        user.setEmail(register.getUsername());
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPhone(register.getPhone());
        user.setRole(register.getRole() != null ? register.getRole() : "USER");
        return user;
    }

    public User toDto(UserEntity entity) {
        User dto = new User();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole());
        dto.setImage(entity.getImage());
        return dto;
    }

    public void updateEntity(UpdateUser updateDto, UserEntity entity) {
        if (updateDto.getFirstName() != null) entity.setFirstName(updateDto.getFirstName());
        if (updateDto.getLastName() != null) entity.setLastName(updateDto.getLastName());
        if (updateDto.getPhone() != null) entity.setPhone(updateDto.getPhone());
    }
}