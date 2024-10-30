package com.example.WebBanSach.services;

import com.example.WebBanSach.entity.Category;
import com.example.WebBanSach.entity.Product;
import com.example.WebBanSach.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories () {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    public void deleteCategoryByIds(List<Long> productIds) {
        for (Long id : productIds) {
            categoryRepository.deleteById(id);
        }
    }
    public Page<Category> searchCategorysByName(String name, Pageable pageable) {
        return categoryRepository.findByTitleContaining(name, pageable);
    }

}
