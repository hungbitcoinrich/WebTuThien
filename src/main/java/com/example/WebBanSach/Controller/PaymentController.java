package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.OrderingMethods;
import com.example.WebBanSach.entity.PaymentMethods;
import com.example.WebBanSach.services.PaymentMethodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PaymentController {
    @Autowired
    private PaymentMethodsService paymentMethodsService;

    @GetMapping("/payment-methods")
//    public String showPaymentMethods() {
//        return "payment-methods"; // Trả về tên của tệp HTML mà bạn đã tạo
//    }
    public String showPaymentMethods(Model model) {
        List<PaymentMethods> paymentMethods = paymentMethodsService.getAllPaymentMethods();
        model.addAttribute("paymentmethods", paymentMethods);
        return "payment-methods";
    }
}