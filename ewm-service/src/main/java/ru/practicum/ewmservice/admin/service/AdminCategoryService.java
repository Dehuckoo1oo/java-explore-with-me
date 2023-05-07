package ru.practicum.ewmservice.admin.service;

import ru.practicum.ewmservice.category.DTO.CategoryDTO;
import ru.practicum.ewmservice.category.DTO.NewCategoryDTO;

public interface AdminCategoryService {
    public CategoryDTO createCategory(NewCategoryDTO newCategoryDTO);

    public CategoryDTO deleteCategory(Integer catId);

    public CategoryDTO updateCategory(Integer catId, NewCategoryDTO newCategoryDTO);
}
