package com.example.WebBanSach.services;

import ch.qos.logback.core.model.Model;
import com.example.WebBanSach.entity.CartItem;
import com.example.WebBanSach.entity.Order;
import com.example.WebBanSach.entity.OrderDetail;
import com.example.WebBanSach.entity.User;
import com.example.WebBanSach.repository.ProductRepository;
import com.example.WebBanSach.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {




    @Autowired
    private final OrderRepository orderRepository;
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }


    public void saveOrder(Order order,User user) {
        order.setUser(user);
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }




    private final ProductRepository productRepository;

    public void createOrder(Order order, List<CartItem> cartItems) {
        // Thiết lập thông tin của đơn hàng từ đối tượng Order được truyền vào
        orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setOrder(order);


            order.getOrderDetails().add(orderDetail);
        }

        orderRepository.save(order);
    }

    public void updateOrderStatus(Long orderId, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            orderRepository.save(order);
        }
    }

    public Order changeOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        order.setStatus(newStatus); // Cập nhật trạng thái mới

        // Xử lý logic nếu cần

        return orderRepository.save(order); // Lưu và trả về đơn hàng đã cập nhật
    }

    // Phương thức để hủy đơn hàng
    public Order cancelOrder(Long orderId) {
        return changeOrderStatus(orderId, "Hủy bỏ");
    }
    public List<Order> getOrdersByDateRange(Date fromDate, Date toDate) {
        return orderRepository.findByOrderDateBetween(fromDate, toDate);
    }

    public double calculateTotalRevenue(Date startDate, Date endDate) {
        if (startDate == null && endDate == null) {
            // Calculate total revenue for all orders if both startDate and endDate are null
            List<Order> allOrders = orderRepository.findAll();
            double totalRevenue = 0.0;
            for (Order order : allOrders) {
                totalRevenue += order.getTotalPrice();
            }
            return totalRevenue;
        } else if (startDate != null && endDate != null) {
            // Calculate total revenue within the specified date range
            if (endDate.before(startDate)) {
                throw new IllegalArgumentException("End date cannot be before start date");
            }
            List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
            double totalRevenue = 0.0;
            for (Order order : orders) {
                totalRevenue += order.getTotalPrice();
            }
            return totalRevenue;
        } else {
            throw new IllegalArgumentException("Invalid parameters");
        }
    }
}

