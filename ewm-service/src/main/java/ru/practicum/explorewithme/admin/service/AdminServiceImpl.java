package ru.practicum.explorewithme.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.admin.exception.AccessDeniedException;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.CategoryMapper;
import ru.practicum.explorewithme.category.service.CategoryService;
import ru.practicum.explorewithme.user.dto.ReturnUserDto;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.model.UserRole;
import ru.practicum.explorewithme.user.service.UserService;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final UserService userService;
    private final CategoryService categoryService;

    //region Admin users methods
    @Override
    public Collection<ReturnUserDto> getUsersById(Long requesterId, List<Long> ids, Integer from, Integer size) {
        validPermissionsOrThrow(requesterId);
        return userService.getUsersByIdWithPagination(ids, from, size);
    }

    @Override
    public ReturnUserDto createUser(Long requesterId, UserDto userDto) {
        validPermissionsOrThrow(requesterId);
        return userService.createUser(userDto);
    }

    @Override
    public void deleteUser(Long requesterId, Long id) {
        validPermissionsOrThrow(requesterId);
        userService.deleteUser(id);
    }

    @Override
    public ReturnUserDto activateUser(Long requesterId, Long id) {
        validPermissionsOrThrow(requesterId);
        return userService.activateUser(id);
    }

    //endregion

    //region Admin category methods

    @Override
    public CategoryDto createCategory(Long requesterId, CategoryDto categoryDto) {
        validPermissionsOrThrow(requesterId);
        return CategoryMapper.toDto(categoryService.createCategory(categoryDto));
    }

    @Override
    public CategoryDto patchCategory(Long requesterId, CategoryDto categoryDto) {
        validPermissionsOrThrow(requesterId);
        return CategoryMapper.toDto(categoryService.patchCategory(categoryDto));
    }

    @Override
    public void deleteCategory(Long requesterId, Long catId) {
        validPermissionsOrThrow(requesterId);
        categoryService.deleteCategory(catId);
    }

    //endregion



    private void validPermissionsOrThrow(Long userId) {
        User user = userService.getUserByIdOrThrow(userId);
        if (user.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException();
        }
    }


}
