package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.Users;

@UtilityClass
public class UserMapper {

    public UserDto mapUsersToUserDto(Users users) {
        return UserDto.builder()
                .id(users.getId())
                .email(users.getEmail())
                .name(users.getName())
                .build();
    }

    public Users mapnewUserRequestToUsers(NewUserRequest newUserRequest) {
        return Users.builder()
                .email(newUserRequest.getEmail())
                .name(newUserRequest.getName())
                .build();
    }

    public UserShortDto mapUsersToUserShortDto(Users users) {
        return UserShortDto.builder()
                .id(users.getId())
                .name(users.getName())
                .build();
    }
}
