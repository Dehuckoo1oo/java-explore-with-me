package ru.practicum.ewmservice.category.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.category.DTO.CategoryDTO;
import ru.practicum.ewmservice.category.service.PublicCategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class PublicCategoryController {
    private PublicCategoryService publicCategoryService;

    public PublicCategoryController(PublicCategoryService publicCategoryService) {
        this.publicCategoryService = publicCategoryService;
    }

    @GetMapping
    public List<CategoryDTO> getCategories(@RequestParam(name = "from", required = false, defaultValue = "0") int from,
                                           @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return publicCategoryService.findCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDTO getCategory(@PathVariable Integer catId) {
        return publicCategoryService.findCategory(catId);
    }

}
