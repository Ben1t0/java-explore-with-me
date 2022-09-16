package ru.practicum.explore_with_me.service.user;

import ru.practicum.explore_with_me.model.user.ReturnUserDto;
import ru.practicum.explore_with_me.model.user.User;
import ru.practicum.explore_with_me.model.user.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAll();

    User getUserByIdOrThrow(Long id);

    UserDto getUserDtoByOrThrow(Long id);

    ReturnUserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    UserDto patchUser(UserDto userDto);

    void deleteUser(Long id);

    Collection<ReturnUserDto> getUsersByIdWithPagination(Collection<Long> ids, Integer from, Integer size);
}
