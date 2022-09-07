package ru.practicum.explorewithme.compilation.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Builder
public class CompilationDto {

    private Long id;
    @NotNull
    private Collection<Long> events;
    @NotNull
    private Boolean pinned;
    @NotNull
    @NotBlank
    private String title;
}
