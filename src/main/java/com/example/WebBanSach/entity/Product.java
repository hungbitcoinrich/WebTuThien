package com.example.WebBanSach.entity;

import com.example.WebBanSach.Validtion.ValidUserId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 100, message = "Tiêu đề phải ít hơn 100 ký tự")
    @Column(name = "title")
    private String title;

    @NotBlank(message = "Tác giả không được để trống")
    @Size(max = 100, message = "Tên tác giả phải ít hơn 100 ký tự")
    @Column(name = "author")
    private String author;

    @Min(value = 1000, message = "Giá phải lớn hơn hoặc bằng 1000")
    @Column(name = "price")
    private double price;

    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    @Column(name = "quantity")
    private double quantity;

    @NotBlank(message = "Chi tiết sản phẩm không được để trống")
    @Size(max = 1000, message = "Chi tiết sản phẩm phải ít hơn 1000 ký tự")
    @Column(name = "detail")
    private String detail;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "img1")
    private String img1;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @ValidUserId
    private User user;

    public boolean equalsExcludingQuantity(Product other) {
        if (this == other) return true;
        if (other == null) return false;
        return title.equals(other.title) &&
                author.equals(other.author) &&
                price == other.price &&
                detail.equals(other.detail) &&
                category.equals(other.category) &&
                img1.equals(other.img1) &&
                user.equals(other.user);
    }

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    // Custom getter for isDeleted
    public boolean isIsDeleted() {
        return isDeleted;
    }

    // Custom setter for isDeleted
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
