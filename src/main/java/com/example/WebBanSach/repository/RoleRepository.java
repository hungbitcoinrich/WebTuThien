package com.example.WebBanSach.repository;

import com.example.WebBanSach.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
//    @Query("SELECT r.id FROM Role r WHERE r.name = ?1")
    Role findByName(String name);
}