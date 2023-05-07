package ru.practicum.ewmservice.admin.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.admin.repository.AdminCategoryRepository;
import ru.practicum.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.category.DTO.CategoryDTO;
import ru.practicum.ewmservice.category.DTO.NewCategoryDTO;
import ru.practicum.ewmservice.exception.NoSuchBodyException;
import ru.practicum.ewmservice.exception.NotFoundResourceException;

import java.util.Optional;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private AdminCategoryRepository adminCategoryRepository;
    private CategoryMapper categoryMapper;

    public AdminCategoryServiceImpl(AdminCategoryRepository adminCategoryRepository, CategoryMapper categoryMapper) {
        this.adminCategoryRepository = adminCategoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDTO createCategory(NewCategoryDTO newCategoryDTO) {
        Category category = adminCategoryRepository.save(categoryMapper.mapNewToEntity(newCategoryDTO));
        return categoryMapper.mapEntityToDTO(category);
    }

    @Override
    public CategoryDTO deleteCategory(Integer catId) {
        Category category = getCategory(catId);
        adminCategoryRepository.delete(category);
        return categoryMapper.mapEntityToDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(Integer catId, NewCategoryDTO newCategoryDTO) {
        Category category = getCategory(catId);
        if (!newCategoryDTO.getName().isBlank()) {
            category.setName(newCategoryDTO.getName());
        }
        adminCategoryRepository.saveAndFlush(category);
        return categoryMapper.mapEntityToDTO(category);
    }

    private Category getCategory(Integer catId) {
        Optional<Category> category = adminCategoryRepository.findById(catId);
        return category.orElseThrow(() -> new NoSuchBodyException(String.format("Category with id=%s was not found",
                catId)));
    }
}
