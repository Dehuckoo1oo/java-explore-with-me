package ru.practicum.ewmservice.category.service;


import ru.practicum.ewmservice.category.DTO.CategoryDTO;

import java.util.List;

public interface PublicCategoryService {
    List<CategoryDTO> findCategories(Integer from, Integer size);

    CategoryDTO findCategory(Integer catId);
}
