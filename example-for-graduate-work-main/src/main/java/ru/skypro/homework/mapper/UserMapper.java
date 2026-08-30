package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "username", target = "email")
    UserEntity toEntity(Register register);

    default User toDto(UserEntity entity) {
        User dto = new User();
        // ... другие поля
        if (entity.getImage() != null) {
            dto.setImage("/uploads/" + entity.getImage());
        }
        return dto;
    }

    default UpdateUser toUpdateDto(UserEntity entity) {
        if (entity == null) return null;
        UpdateUser dto = new UpdateUser();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        return dto;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget UserEntity entity, UpdateUser dto);
}