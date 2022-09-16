package ru.practicum.explore_with_me.service.category;

import ru.practicum.explore_with_me.model.category.Category;
import ru.practicum.explore_with_me.model.category.CategoryDto;

import java.util.Collection;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto patchCategory(CategoryDto categoryDto);

    CategoryDto getCategoryDtoByIdOrThrow(Long id);

    Category getCategoryByIdOrThrow(Long id);

    Collection<CategoryDto> getAll(Integer from, Integer size);
}
