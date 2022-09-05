package ru.practicum.explorewithme.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.user.dto.ReturnUserDto;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.dto.UserMapper;
import ru.practicum.explorewithme.user.exception.UserNotFoundException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.model.UserRole;
import ru.practicum.explorewithme.user.model.UserState;
import ru.practicum.explorewithme.user.repository.UserRepository;
import ru.practicum.explorewithme.utils.OffsetBasedPageRequest;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//TODO: check functions

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public Collection<ReturnUserDto> getUsersByIdWithPagination(List<Long> ids, Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("id"));
        return userRepository.findAllByIdIn(ids, page).stream()
                .map(UserMapper::toReturnUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserDto getUserDtoByOrThrow(Long id) {
        return UserMapper.toUserDto(getUserByIdOrThrow(id));
    }

    @Override
    public ReturnUserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setRole(UserRole.USER);
        user.setState(UserState.INACTIVE);
        return UserMapper.toReturnUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        getUserByIdOrThrow(userDto.getId());
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto patchUser(UserDto userDto) {
        User user = getUserByIdOrThrow(userDto.getId());

        User userToUpdate = User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        if (userDto.getEmail() != null) {
            userToUpdate.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            userToUpdate.setName(userDto.getName());
        }

        return UserMapper.toUserDto(userRepository.save(userToUpdate));
    }

    @Override
    public ReturnUserDto activateUser(Long id) {
        User user = getUserByIdOrThrow(id);
        user.setState(UserState.ACTIVE);

        return UserMapper.toReturnUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        getUserByIdOrThrow(id);
        userRepository.deleteById(id);
    }
}
