package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.Product;
import com.example.WebBanSach.entity.ProductReview;
import com.example.WebBanSach.entity.User;
import com.example.WebBanSach.services.ProductReviewService;
import com.example.WebBanSach.services.ProductService;
import com.example.WebBanSach.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;


@Controller
public class    ProductController {
    private final String UPLOAD_DIR = "src/main/resources/static/Admin/img/products/";

    @Autowired
    private ProductService productService;

    @Autowired
    private UserServices userService;

    @Autowired
    private ProductReviewService productReviewService;

    @GetMapping("/")
    public String index(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "Customer/index";
    }

    @GetMapping("/category")
    public String showCategory(Model model) {
        return "Customer/category";
    }

    @GetMapping("/product")
    public String showProduct(Model model) {
        return "Customer/product";
    }

    @GetMapping("/contact")
    public String showContact(Model model) {
        return "Customer/contact";
    }
    @GetMapping("/post")
    public String showPost(Model model) {
        return "Customer/post";
    }

    @GetMapping("/product/detail/{id}")
    public String getProductDetail(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);

            // Fetch related products
            List<Product> relatedProducts = productService.findRelatedProducts(product.getCategory().getId(), id);
            model.addAttribute("relatedProducts", relatedProducts);

            // Fetch reviews for the product
            List<ProductReview> reviews = productReviewService.getReviewsByProductId(id);
            model.addAttribute("reviews", reviews);

            // Fetch current user details
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("currentUser", user);

            // Optional: Set current rating for form (if needed)
            model.addAttribute("currentRating", 0); // Set to 0 or fetch the actual rating if needed

            return "Customer/productDetail";
        } else {
            return "redirect:/";
        }
    }


    @PostMapping("/product/review")
    public String submitReview(@RequestParam("productId") Long productId,
                               @RequestParam("rating") int rating,
                               @RequestParam("comment") String comment,
                               Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        Optional<Product> productOptional = productService.getProductById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductReview review = new ProductReview();
            review.setProduct(product);
            review.setUser(user);
            review.setRating(rating);
            review.setComment(comment);
            productReviewService.saveReview(review);
        }

        // Redirect to the product detail page after submitting the review
        return "redirect:/product/detail/" + productId;
    }

}
