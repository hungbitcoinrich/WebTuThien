package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.User;
import com.example.WebBanSach.repository.IUserRepository;
import com.example.WebBanSach.services.UserServices;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserServices userService;

    @Autowired
    private  IUserRepository userRepository;
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "user/register";
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }


    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @GetMapping("/lock/{id}")
    public String lockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.lockUser(id);
        redirectAttributes.addFlashAttribute("message", "Khóa tài khoản thành công");
        return "redirect:/Admin/users";
    }

    @GetMapping("/unlock/{id}")
    public String unlockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.unlockUser(id);
        redirectAttributes.addFlashAttribute("message", "Mở khóa tài khoản thành công");
        return "redirect:/Admin/users";
    }

}

