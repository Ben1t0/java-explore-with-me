package ru.practicum.explorewithme.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.admin.service.AdminService;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.user.dto.ReturnUserDto;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.validation.Validation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final AdminService adminService;


    //region /Admin/Users Handlers

    @GetMapping("/users")
    public Collection<ReturnUserDto> getUsersById(@RequestHeader("X-User-Id") Long requesterId,
                                                  @RequestParam(value = "ids") List<Long> ids,
                                                  @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return adminService.getUsersById(requesterId, ids, from, size);
    }

    @PostMapping("/users")
    @Validated(Validation.OnCreate.class)
    public ReturnUserDto createUser(@RequestHeader("X-User-Id") Long requesterId,
                                    @Valid @RequestBody UserDto userDto) {
        return adminService.createUser(requesterId, userDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@RequestHeader("X-User-Id") Long requesterId,
                           @PathVariable(name = "userId") Long id) {
        adminService.deleteUser(requesterId, id);
    }

    @PatchMapping("/users/{userId}/activate")
    public ReturnUserDto activateUser(@RequestHeader("X-User-Id") Long requesterId,
                                      @PathVariable(name = "userId") Long id) {
        return adminService.activateUser(requesterId, id);
    }
    //endregion

    //region /Admin/Categories Handlers

    @PostMapping("/categories")
    @Validated(Validation.OnCreate.class)
    public CategoryDto createCategory(@RequestHeader("X-User-Id") Long requesterId,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        return adminService.createCategory(requesterId, categoryDto);
    }

    @PatchMapping("/categories")
    @Validated(Validation.OnPatch.class)
    public CategoryDto patchCategory(@RequestHeader("X-User-Id") Long requesterId,
                                     @Valid @RequestBody CategoryDto categoryDto) {
        return adminService.patchCategory(requesterId, categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@RequestHeader("X-User-Id") Long requesterId,
                               @PathVariable Long catId) {
        adminService.deleteCategory(requesterId, catId);
    }

    //endregion
}
