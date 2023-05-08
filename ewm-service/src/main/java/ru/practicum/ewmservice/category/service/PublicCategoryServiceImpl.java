package ru.practicum.ewmservice.category.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.category.DTO.CategoryDTO;
import ru.practicum.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.category.repository.PublicCategoryRepository;
import ru.practicum.ewmservice.exception.NoSuchBodyException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final PublicCategoryRepository publicCategoryRepository;
    private final CategoryMapper categoryMapper;

    public PublicCategoryServiceImpl(PublicCategoryRepository publicCategoryRepository, CategoryMapper categoryMapper) {
        this.publicCategoryRepository = publicCategoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDTO> findCategories(Integer from, Integer size) {
        Page<Category> categories = publicCategoryRepository.findAll(PageRequest.of(from, size));
        return categories.stream().map(cat -> categoryMapper.mapEntityToDTO(cat)).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO findCategory(Integer catId) {
        Category category = publicCategoryRepository.findById(catId).orElseThrow(() -> new NoSuchBodyException(
                String.format("Category with id=%s was not found", catId)));
        return categoryMapper.mapEntityToDTO(category);
    }
}
