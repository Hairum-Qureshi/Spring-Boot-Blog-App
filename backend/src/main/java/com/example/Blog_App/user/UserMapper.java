package com.example.Blog_App.user;

import com.example.Blog_App.user.dtos.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "profilePicture", ignore = true)
    @Mapping(target = "biography", ignore = true)
    UserEntity toEntity(UserDto dto);

    UserDto toDto(UserEntity entity);
}
