package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.Category;
import com.example.WebBanSach.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

public class CategoryController {

    @Autowired
    private CategoryService categoryService;


}
