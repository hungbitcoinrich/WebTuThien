package com.example.WebBanSach.services;

import com.example.WebBanSach.entity.OrderingMethods;
import com.example.WebBanSach.entity.ShippingMethods;
import com.example.WebBanSach.repository.ShippingMethodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShippingMethodsService {
    @Autowired
    private ShippingMethodsRepository shippingMethodsRepository;

    public List<ShippingMethods> getAllShippingMethods() {
        return shippingMethodsRepository.findAll();
    }

    public Optional<ShippingMethods> getShippingMethodsById(Long id) {
        return shippingMethodsRepository.findById(id);
    }

    public void saveShippingMethods(ShippingMethods shippingMethods) {
        shippingMethodsRepository.save(shippingMethods);
    }

    public void deleteShippingMethods(Long id) {
        shippingMethodsRepository.deleteById(id);
    }

    public void updateShippingMethods(ShippingMethods shippingMethods) {
        shippingMethodsRepository.save(shippingMethods);
    }
}
