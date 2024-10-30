package com.example.WebBanSach.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    // Getters and setters
}
