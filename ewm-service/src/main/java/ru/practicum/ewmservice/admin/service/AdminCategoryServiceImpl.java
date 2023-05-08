package ru.practicum.ewmservice.admin.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.admin.repository.AdminCategoryRepository;
import ru.practicum.ewmservice.admin.repository.AdminEventRepository;
import ru.practicum.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.category.DTO.CategoryDTO;
import ru.practicum.ewmservice.category.DTO.NewCategoryDTO;
import ru.practicum.ewmservice.exception.ConflictException;
import ru.practicum.ewmservice.exception.NoSuchBodyException;

import java.util.Optional;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private AdminCategoryRepository adminCategoryRepository;
    private AdminEventRepository adminEventRepository;
    private CategoryMapper categoryMapper;

    public AdminCategoryServiceImpl(AdminCategoryRepository adminCategoryRepository, CategoryMapper categoryMapper,
                                    AdminEventRepository adminEventRepository) {
        this.adminCategoryRepository = adminCategoryRepository;
        this.categoryMapper = categoryMapper;
        this.adminEventRepository = adminEventRepository;
    }

    @Override
    public CategoryDTO createCategory(NewCategoryDTO newCategoryDTO) {
        checkCategory(newCategoryDTO);
        Category category = adminCategoryRepository.save(categoryMapper.mapNewToEntity(newCategoryDTO));
        return categoryMapper.mapEntityToDTO(category);
    }

    @Override
    public CategoryDTO deleteCategory(Integer catId) {
        Category category = getCategory(catId);
        if (adminEventRepository.findByCategory(category).size() > 0) {
            throw new ConflictException("В категории присутствуют события. Невозможно удалить.");
        }
        adminCategoryRepository.delete(category);
        return categoryMapper.mapEntityToDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(Integer catId, NewCategoryDTO newCategoryDTO) {
        checkCategory(newCategoryDTO);
        Category category = getCategory(catId);
        if (!newCategoryDTO.getName().isBlank()) {
            category.setName(newCategoryDTO.getName());
        }
        adminCategoryRepository.saveAndFlush(category);
        return categoryMapper.mapEntityToDTO(category);
    }

    private void checkCategory(NewCategoryDTO newCategoryDTO) {
        if (adminCategoryRepository.findByName(newCategoryDTO.getName()).isPresent()) {
            throw new ConflictException("Категория с названием " + newCategoryDTO.getName() + " уже существует");
        }
    }

    private Category getCategory(Integer catId) {
        Optional<Category> category = adminCategoryRepository.findById(catId);
        return category.orElseThrow(() -> new NoSuchBodyException(String.format("Category with id=%s was not found",
                catId)));
    }
}
