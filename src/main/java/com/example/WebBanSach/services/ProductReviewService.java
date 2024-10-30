package com.example.WebBanSach.services;

import com.example.WebBanSach.entity.ProductReview;
import com.example.WebBanSach.repository.ProductReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductReviewService {

    @Autowired
    private ProductReviewRepository productReviewRepository;

    public void saveReview(ProductReview review) {
        productReviewRepository.save(review);
    }

    public List<ProductReview> getReviewsByProductId(Long productId) {
        return productReviewRepository.findByProductId(productId);
    }
}
