package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.Policy;
import com.example.WebBanSach.services.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ReturnPolicyController {

    @Autowired
    private PolicyService policyService;
//    @GetMapping("/return-policy")
//    public String returnPolicy() {
//        return "return-policy"; // This should match the Thymeleaf template name (return-policy.html)
//    }
    @GetMapping("/return-policy")
    public String showPolicies(Model model) {
        List<Policy> policies = policyService.getAllPolicies();
        model.addAttribute("policies", policies);
        return "return-policy";
    }
}
