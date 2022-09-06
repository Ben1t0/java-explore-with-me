package ru.practicum.explorewithme.category.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long catId) {
        super(String.format("Category with id %d not found", catId));
    }
}
