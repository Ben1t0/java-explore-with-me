package ru.practicum.explorewithme.admin.service;

import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.user.dto.ReturnUserDto;
import ru.practicum.explorewithme.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface AdminService {
    Collection<ReturnUserDto> getUsersById(Long requesterId, List<Long> ids, Integer from, Integer size);

    ReturnUserDto createUser(Long requesterId, UserDto userDto);

    void deleteUser(Long requesterId, Long id);

    ReturnUserDto activateUser(Long requesterId, Long id);

    CategoryDto createCategory(Long requesterId, CategoryDto categoryDto);

    CategoryDto patchCategory(Long requesterId, CategoryDto categoryDto);

    void deleteCategory(Long requesterId, Long catId);
}
