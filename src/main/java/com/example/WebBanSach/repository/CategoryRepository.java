package com.example.WebBanSach.repository;

import com.example.WebBanSach.entity.Category;

import com.example.WebBanSach.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAll(Pageable pageable);
    @Query("SELECT p FROM Category p WHERE p.name LIKE %:name%")
    Page<Category> findByTitleContaining(@Param("name") String name, Pageable pageable);
}
