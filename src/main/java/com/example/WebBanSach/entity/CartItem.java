package com.example.WebBanSach.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "CartItem")
@RequiredArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CARTITEM")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_PRO")
    private Product product;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    // Constructors
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
