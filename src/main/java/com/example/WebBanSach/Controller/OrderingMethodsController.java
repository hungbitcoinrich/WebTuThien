package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.OrderingMethods;
import com.example.WebBanSach.services.OrderingMethodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OrderingMethodsController {

    @Autowired
    private OrderingMethodsService orderingMethodsService;
//    @GetMapping("/ordering-methods")
//    public String orderingMethods() {
//        return "ordering-methods"; // This should match the Thymeleaf template name (ordering-methods.html)
//    }
    @GetMapping("/ordering-methods")
    public String showOrderingMethods(Model model) {
        List<OrderingMethods> orderingMethods = orderingMethodsService.getAllOrderingMethods();
        model.addAttribute("orderingmethods", orderingMethods);
        return "ordering-methods";
    }
}
