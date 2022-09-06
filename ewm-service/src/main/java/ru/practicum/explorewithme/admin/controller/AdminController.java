package ru.practicum.explorewithme.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.service.CategoryService;
import ru.practicum.explorewithme.user.dto.ReturnUserDto;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.service.UserService;
import ru.practicum.explorewithme.validation.Validation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;

    //region /Admin/Users Handlers

    @GetMapping("/users")
    public Collection<ReturnUserDto> getUsersById(@RequestParam(value = "ids") List<Long> ids,
                                                  @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return userService.getUsersByIdWithPagination(ids, from, size);
    }

    @PostMapping("/users")
    @Validated(Validation.OnCreate.class)
    public ReturnUserDto createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable(name = "userId") Long id) {
        userService.deleteUser(id);
    }

    @PatchMapping("/users/{userId}/activate")
    public ReturnUserDto activateUser(@PathVariable(name = "userId") Long id) {
        return userService.activateUser(id);
    }
    //endregion

    //region /Admin/Categories Handlers

    @PostMapping("/categories")
    @Validated(Validation.OnCreate.class)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping("/categories")
    @Validated(Validation.OnPatch.class)
    public CategoryDto patchCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.patchCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }

    //endregion
}
