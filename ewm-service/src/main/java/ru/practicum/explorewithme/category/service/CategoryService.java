package ru.practicum.explorewithme.category.service;

import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.model.Category;

import java.util.Collection;

public interface CategoryService {

    Category createCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    Category patchCategory(CategoryDto categoryDto);

    CategoryDto getById(Long id);

    Collection<CategoryDto> getAll(Integer from, Integer size);
}
