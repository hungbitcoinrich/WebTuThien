package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.ShippingMethods;
import com.example.WebBanSach.services.ShippingMethodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShippingMethodsController {

    @Autowired
    private ShippingMethodsService shippingMethodsService;
    @GetMapping("/shipping-methods")
//    public String shippingMethods() {
//        return "shipping-methods"; // This should match the Thymeleaf template name (shipping-methods.html)
//    }
    public String shippingMethods(Model model) {
        List<ShippingMethods> shippingMethods = shippingMethodsService.getAllShippingMethods();
        model.addAttribute("shippingmethods", shippingMethods);
        return "shipping-methods";
    }
}
