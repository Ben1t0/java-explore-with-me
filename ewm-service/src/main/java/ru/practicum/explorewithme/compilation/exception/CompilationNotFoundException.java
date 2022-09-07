package ru.practicum.explorewithme.compilation.exception;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(long catId) {
        super(String.format("Category with id %d not found", catId));
    }
}
