package ru.practicum.explorewithme.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.admin.exception.AccessDeniedException;
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

    private void validPermissionsOrThrow(Long userId) {
        User user = userService.getUserByIdOrThrow(userId);
        if (user.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException();
        }
    }
}
