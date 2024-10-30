package com.example.WebBanSach.services;

import com.example.WebBanSach.entity.CartItem;
import com.example.WebBanSach.entity.Product;
import com.example.WebBanSach.repository.CartRepository;
import com.example.WebBanSach.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope
public class CartService {
    private List<CartItem> cartItems = new ArrayList<>();

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    public void addToCart(long productId, int quantity) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Check if the product is already in the cart
            CartItem existingItem = cartItems.stream()
                    .filter(item -> item.getProduct().getId() == productId)
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                updateCartItem(existingItem);
            } else {
                CartItem newItem = new CartItem(product, quantity);
                cartItems.add(newItem);
                cartRepository.save(newItem);
            }
        } else {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
        cartRepository.deleteAll();
    }

    public double calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public void updateCartItemQuantity(Long itemId, int quantity) {
        CartItem item = getCartItem(itemId);
        if (item != null) {
            if (quantity > 0) {
                item.setQuantity(quantity);
                updateCartItem(item);
            } else {
                removeFromCart(Math.toIntExact(itemId));
            }
        }
    }

    public void removeFromCart(int itemId) {
        cartItems.removeIf(item -> item.getId() == itemId);
        cartRepository.deleteById((long) itemId);
    }

    public CartItem getCartItem(Long itemId) {
        return cartItems.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElse(null);
    }

    public void updateCartItem(CartItem cartItem) {
        cartRepository.save(cartItem);
    }
}
