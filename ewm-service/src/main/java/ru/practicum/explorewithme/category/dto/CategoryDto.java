package ru.practicum.explorewithme.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.validation.Validation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @NotNull(groups = Validation.OnPatch.class)
    private Long id;
    @NotNull
    @NotBlank(groups = {Validation.OnCreate.class, Validation.OnPatch.class}, message = "Category name can't be blank")
    private String name;
}
