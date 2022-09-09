package ru.practicum.explorewithme.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReturnUserDto {
    private Long id;
    private String name;
    private String email;
}
