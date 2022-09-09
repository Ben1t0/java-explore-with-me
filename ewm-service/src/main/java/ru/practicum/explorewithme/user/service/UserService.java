package ru.practicum.explorewithme.user.service;

import ru.practicum.explorewithme.user.dto.ReturnUserDto;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.model.User;

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

    ReturnUserDto activateUser(Long id);
}
