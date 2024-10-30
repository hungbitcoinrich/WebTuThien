package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.*;
import com.example.WebBanSach.services.CartService;
import com.example.WebBanSach.services.OrderService;
import com.example.WebBanSach.services.ProductService; // Add this import
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService; // Add this field

    @GetMapping("/checkout")
    public String checkout() {
        return "/cart/checkout";
    }

    @PostMapping("/submit")
    public String submitOrder(@RequestParam String customerName,
                              @RequestParam String shippingAddress,
                              @RequestParam String phoneNumber,
                              @RequestParam String email,
                              @RequestParam String notes,
                              @RequestParam String paymentMethod,
                              Model model) {
        try {
        List<CartItem> cartItems = cartService.getCartItems();
        double totalPrice = cartService.calculateTotalPrice(cartItems);

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setShippingAddress(shippingAddress);
        order.setPhoneNumber(phoneNumber);
        order.setEmail(email);
        order.setNotes(notes);
        order.setPaymentMethod(paymentMethod);
        order.setTotalPrice(totalPrice);
        order.setOrderDate(new Date());

        int totalQuantity = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            if (product.getQuantity() < quantity) {
                model.addAttribute("error", "Sản phẩm " + product.getTitle() + " không đủ số lượng trong kho.");
                model.addAttribute("cartItems", cartItems);
                return "cart/checkout";
            }

            product.setQuantity(product.getQuantity() - quantity);
            productService.updateProduct(product);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setOrder(order);
            orderDetail.setPaymentMethod(paymentMethod);
            orderDetail.setShippingAddress(shippingAddress);
            orderDetail.setPhoneNumber(phoneNumber);

            order.getOrderDetails().add(orderDetail);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetail) {
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
            orderService.saveOrder(order, customUserDetail.getUser());
        }
        if ("VNPay".equals(paymentMethod)) {
            return "redirect:/api/payment/vnpay_payment";
        }
        cartService.clearCart();

        return "redirect:/order/confirmation";
    } catch (Exception e) {
        model.addAttribute("error", "Đã xảy ra lỗi trong quá trình xử lý đơn hàng.");
        return "cart/checkout";
    }
    }

    @GetMapping("/vnpay_return")
    public String vnpayReturn(@RequestParam Map<String,String> allParams, Model model, HttpServletRequest request ) {
        // Xử lý kết quả thanh toán từ VNPay
        if (allParams.containsKey("vnp_ResponseCode") && "00".equals(allParams.get("vnp_ResponseCode"))) {
            // Thanh toán thành công
            model.addAttribute("paidAmount", allParams.get("vnp_Amount"));
            return "redirect:/order/confirmation";
        } else {
            // Thanh toán thất bại hoặc không có thông tin vnp_ResponseCode
            model.addAttribute("error", "VNPay payment failed or incomplete.");
            return "redirect:/order/vnp_return"; // Redirect to checkout or another appropriate page
        }
    }


    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }

    @GetMapping("/list")
    public String getOrdersByDateRange(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            Model model) {
        List<Order> orders = orderService.getOrdersByDateRange(fromDate, toDate);
        model.addAttribute("orders", orders);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        return "Admin/orders";
    }

    @GetMapping("/revenue")
    public String getTotalRevenueByDateRange(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            Model model) {
        double totalRevenue = orderService.calculateTotalRevenue(fromDate, toDate);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        return "Admin/revenue";
    }

    @GetMapping("/report")
    public String showRevenueReportForm(Model model) {
        model.addAttribute("fromDate", LocalDate.now());
        model.addAttribute("toDate", LocalDate.now());
        return "Admin/revenue-report";
    }

    @PostMapping("/report")
    public String generateRevenueReport(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            Model model) {
        try {
            List<Order> orders = orderService.getOrdersByDateRange(fromDate, toDate);
            double totalRevenue = orderService.calculateTotalRevenue(fromDate, toDate);

            model.addAttribute("fromDate", fromDate);
            model.addAttribute("toDate", toDate);
            model.addAttribute("totalRevenue", totalRevenue);
            model.addAttribute("orders", orders);

            return "Admin/revenue-report";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while processing revenue report.");
            return "Admin/revenue-report";
        }
    }

    @GetMapping("/reset")
    public String resetTotal(Model model) {
        return "redirect:/Admin/report";
    }


}
