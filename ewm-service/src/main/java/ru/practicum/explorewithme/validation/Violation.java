package ru.practicum.explorewithme.validation;

import lombok.Data;

@Data
public class Violation {
    private final String fieldName;
    private final String message;
}
