package ru.practicum.explorewithme.user.dto;

import ru.practicum.explorewithme.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static ReturnUserDto toReturnUserDto(User user) {
        return ReturnUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .state(user.getState().toString())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }
}
