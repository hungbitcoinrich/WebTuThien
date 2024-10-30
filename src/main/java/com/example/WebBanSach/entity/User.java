package com.example.WebBanSach.entity;

import com.example.WebBanSach.Validtion.ValidUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users") 
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(max = 50, message = "Tên đăng nhập phải ít hơn 50 ký tự")
    @ValidUsername
    private String username;

    @Column(name = "password", length = 250, nullable = false)
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @Column(name = "email", length = 50)
    @Size(max = 50, message = "Email phải ít hơn 50 ký tự")
    private String email;

    @Column(name = "name", length = 50, nullable = false)
    @Size(max = 50, message = "Tên của bạn phải ít hơn 50 ký tự")
    @NotBlank(message = "Tên của bạn không được để trống")
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Product> products;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
//    private Set<Role> roles;
@Column(name = "enabled", nullable = false)
private boolean enabled = true;


}
