package com.example.WebBanSach.Controller;

import com.example.WebBanSach.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendContact(@RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("phone") String phone,
                              @RequestParam("message") String message,
                              RedirectAttributes redirectAttributes) {
        try {
            String subject = "Contact Form Submission from " + name;
            emailService.sendEmail("nguyenquanghung28012003@gmail.com", subject, name, email, phone, message);
            redirectAttributes.addFlashAttribute("success", "Your message has been sent successfully!");
        } catch (MessagingException e) {
            redirectAttributes.addFlashAttribute("error", "There was an error sending your message. Please try again.");
        }
        return "redirect:/contact";
    }
}