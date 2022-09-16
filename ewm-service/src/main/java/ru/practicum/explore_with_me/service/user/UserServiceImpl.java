package ru.practicum.explore_with_me.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.exception.UserNotFoundException;
import ru.practicum.explore_with_me.model.user.ReturnUserDto;
import ru.practicum.explore_with_me.model.user.User;
import ru.practicum.explore_with_me.model.user.UserDto;
import ru.practicum.explore_with_me.model.user.UserMapper;
import ru.practicum.explore_with_me.repository.UserRepository;
import ru.practicum.explore_with_me.utils.OffsetBasedPageRequest;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public Collection<ReturnUserDto> getUsersByIdWithPagination(Collection<Long> ids, Integer from, Integer size) {
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
    public void deleteUser(Long id) {
        getUserByIdOrThrow(id);
        userRepository.deleteById(id);
    }
}
