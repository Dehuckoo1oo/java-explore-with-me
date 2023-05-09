package ru.practicum.ewmservice.category.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCategoryDTO {
    @NotBlank
    private String name;
}
