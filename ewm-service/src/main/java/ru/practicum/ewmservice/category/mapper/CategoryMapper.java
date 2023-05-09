package ru.practicum.ewmservice.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.category.DTO.CategoryDTO;
import ru.practicum.ewmservice.category.DTO.NewCategoryDTO;

@Component
public class CategoryMapper {

    public Category mapNewToEntity(NewCategoryDTO newCategoryDTO) {
        Category category = Category.builder()
                .name(newCategoryDTO.getName())
                .build();
        return category;
    }

    public CategoryDTO mapEntityToDTO(Category category) {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
        return categoryDTO;
    }
}
