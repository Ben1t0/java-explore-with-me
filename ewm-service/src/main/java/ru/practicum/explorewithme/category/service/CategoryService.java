package ru.practicum.explorewithme.category.service;

import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.model.Category;

import java.util.Collection;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto patchCategory(CategoryDto categoryDto);

    CategoryDto getCategoryDtoByIdOrThrow(Long id);

    Category getCategoryByIdOrThrow(Long id);

    Collection<CategoryDto> getAll(Integer from, Integer size);
}
