package ru.practicum.explore_with_me.model.user;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        if (user != null) {
            return UserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        } else {
            return null;
        }
    }

    public static ReturnUserDto toReturnUserDto(User user) {
        if (user != null) {
            return ReturnUserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        } else {
            return null;
        }
    }

    public static User toUser(UserDto userDto) {
        if (userDto != null) {
            return User.builder()
                    .id(userDto.getId())
                    .email(userDto.getEmail())
                    .name(userDto.getName())
                    .build();
        } else {
            return null;
        }
    }
}
