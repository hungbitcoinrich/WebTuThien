package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.UserGuides;
import com.example.WebBanSach.services.UserGuidesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserGuideController {

    @Autowired
    private UserGuidesService userGuidesService;
    @GetMapping("/user-guide")
//    public String userGuide() {
//        return "user-guide"; // This should match the Thymeleaf template name (user-guide.html)
//    }
    public String userGuide(Model model) {
        List<UserGuides> userGuides = userGuidesService.getAllUserGuides();
        model.addAttribute("userguides", userGuides);
        return "user-guide";
    }
}
