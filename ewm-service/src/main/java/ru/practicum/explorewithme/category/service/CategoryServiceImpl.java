package ru.practicum.explorewithme.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.CategoryMapper;
import ru.practicum.explorewithme.category.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.utils.OffsetBasedPageRequest;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDto categoryDto) {
        return categoryRepository.save(CategoryMapper.fromDto(categoryDto));
    }

    @Override
    public void deleteCategory(Long catId) {
        getCategoryOrThrow(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    public Category patchCategory(CategoryDto categoryDto) {
        Category cat = getCategoryOrThrow(categoryDto.getId());
        cat.setName(categoryDto.getName());
        return categoryRepository.save(cat);
    }

    @Override
    public Collection<CategoryDto> getAll(Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size, Sort.by("id"));
        return categoryRepository.findAll(page).stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        return CategoryMapper.toDto(getCategoryOrThrow(id));
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

}
