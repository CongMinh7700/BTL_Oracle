package com.test.library.service.impl;

import com.test.library.dto.CategoryDto;
import com.test.library.model.Category;
import com.test.library.repository.CategoryRepository;
import com.test.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {

        Category categorySave = new Category();
        categorySave.setCate_name(category.getCate_name());
        categorySave.set_activated(true);
        categorySave.set_deleted(false);
        return categoryRepository.save(categorySave);

    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category update(Category category) {
        Category categoryUpdate = categoryRepository.getReferenceById(category.getId());
        categoryUpdate.setCate_name(category.getCate_name());
        return categoryRepository.save(categoryUpdate);
    }

    @Override
    public void deletedById(Long id) {
        Category category = categoryRepository.getById(id);

        category.set_deleted(true);
        category.set_activated(false);
        categoryRepository.save(category);
    }

    @Override
    public void enabledById(Long id) {
        Category category = categoryRepository.getById(id);
        category.set_deleted(false);
        category.set_activated(true);
        categoryRepository.save(category);
    }

    @Override
    public List<Category> findAllByActivated() {

        return categoryRepository.findAllByActivated();
    }

    @Override
    public List<CategoryDto> getCategoriesAndSize() {
         List<CategoryDto> categories = categoryRepository.getCategoriesAndSize();
        return categories;
    }
}
