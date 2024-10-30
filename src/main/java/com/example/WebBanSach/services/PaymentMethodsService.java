package com.example.WebBanSach.services;

import com.example.WebBanSach.entity.OrderingMethods;
import com.example.WebBanSach.entity.PaymentMethods;
import com.example.WebBanSach.repository.PaymentMethodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodsService {
    @Autowired
    private PaymentMethodsRepository paymentMethodsRepository;

    public List<PaymentMethods> getAllPaymentMethods() {
        return paymentMethodsRepository.findAll();
    }

    public Optional<PaymentMethods> getPaymentMethodsById(Long id) {
        return paymentMethodsRepository.findById(id);
    }

    public void savePaymentMethods(PaymentMethods paymentMethods) {
        paymentMethodsRepository.save(paymentMethods);
    }

    public void deletePaymentMethods(Long id) {
        paymentMethodsRepository.deleteById(id);
    }

    public void updatePaymentMethods(PaymentMethods paymentMethods) {
        paymentMethodsRepository.save(paymentMethods);
    }
}
