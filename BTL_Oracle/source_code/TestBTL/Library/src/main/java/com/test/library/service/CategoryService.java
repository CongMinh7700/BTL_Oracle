package com.test.library.service;

import com.test.library.dto.CategoryDto;
import com.test.library.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();
    Category save(Category category);
    Optional<Category> findById(Long id);
    Category update(Category category);
    void deletedById(Long id);
    void enabledById(Long id);
    List<Category> findAllByActivated();

    //Customer
    List<CategoryDto> getCategoriesAndSize();

}
