package com.example.WebBanSach.services;

import com.example.WebBanSach.entity.Product;
import com.example.WebBanSach.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void addProduct(Product product) {
        product.setIsDeleted(false);
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }
    public void deleteProductsByIds(List<Long> productIds) {
        for (Long id : productIds) {
            productRepository.deleteById(id);
        }
    }
    public Product getProductByTitle(String title) {
        return productRepository.findByTitle(title);
    }
    public Page<Product> searchProductsByTitle(String title, Pageable pageable) {
        return productRepository.findByTitleContaining(title, pageable);
    }

    public List<Product> findRelatedProducts(Long categoryId, Long productId) {
        return productRepository.findByCategoryIdAndIdNot(categoryId, productId);
    }
    @Transactional
    public void deleteSoftProductsByIds(List<Long> productIds) {
        for (Long productId : productIds) {
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                product.setIsDeleted(true);
                productRepository.save(product);
            }
        }
    }

    @Transactional
    public void deleteSoftProductById(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setIsDeleted(true);
            productRepository.save(product);
        }
    }
}
