package com.example.WebBanSach.repository;

import com.example.WebBanSach.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    Product findByTitle(String title);
    @Query("SELECT p FROM Product p WHERE p.title LIKE %:title%")
    Page<Product> findByTitleContaining(@Param("title") String title, Pageable pageable);
    List<Product> findByCategoryIdAndIdNot(Long categoryId, Long productId);
}
