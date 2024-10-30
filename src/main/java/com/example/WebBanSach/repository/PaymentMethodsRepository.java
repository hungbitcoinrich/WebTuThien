package com.example.WebBanSach.repository;

import com.example.WebBanSach.entity.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodsRepository extends JpaRepository<PaymentMethods, Long> {
}
