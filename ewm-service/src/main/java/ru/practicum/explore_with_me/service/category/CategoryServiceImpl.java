package ru.practicum.explore_with_me.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.exception.CategoryNotFoundException;
import ru.practicum.explore_with_me.model.category.Category;
import ru.practicum.explore_with_me.model.category.CategoryDto;
import ru.practicum.explore_with_me.model.category.CategoryMapper;
import ru.practicum.explore_with_me.repository.CategoryRepository;
import ru.practicum.explore_with_me.utils.OffsetBasedPageRequest;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return CategoryMapper.toDto(categoryRepository.save(CategoryMapper.fromDto(categoryDto)));
    }

    @Override
    public void deleteCategory(Long catId) {
        getCategoryByIdOrThrow(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        Category cat = getCategoryByIdOrThrow(categoryDto.getId());
        cat.setName(categoryDto.getName());
        return CategoryMapper.toDto(categoryRepository.save(cat));
    }

    @Override
    public Collection<CategoryDto> getAll(Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("id"));
        return categoryRepository.findAll(page).stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryDtoByIdOrThrow(Long id) {
        return CategoryMapper.toDto(getCategoryByIdOrThrow(id));
    }

    @Override
    public Category getCategoryByIdOrThrow(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

}
