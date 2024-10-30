package com.example.WebBanSach.services;


import com.example.WebBanSach.entity.OrderingMethods;
import com.example.WebBanSach.repository.OrderingMethodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderingMethodsService {
    @Autowired
    private OrderingMethodsRepository orderingMethodsRepository;

    public List<OrderingMethods> getAllOrderingMethods() {
        return orderingMethodsRepository.findAll();
    }

    public Optional<OrderingMethods> getOrderingMethodsById(Long id) {
        return orderingMethodsRepository.findById(id);
    }

    public void saveOrderingMethods(OrderingMethods orderingMethods) {
        orderingMethodsRepository.save(orderingMethods);
    }

    public void deleteOrderingMethods(Long id) {
        orderingMethodsRepository.deleteById(id);
    }

    public void updateOrderingMethods(OrderingMethods orderingMethods) {
        orderingMethodsRepository.save(orderingMethods);
    }
}
