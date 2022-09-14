package ru.practicum.explorewithme.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.service.CategoryService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryDto> getAllCategories(@RequestParam(value = "from", defaultValue = "0") Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        return categoryService.getCategoryDtoByIdOrThrow(catId);
    }
}
