package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.PasswordResetToken;
import com.example.WebBanSach.repository.PasswordResetTokenRepository;
import com.example.WebBanSach.services.EmailService;
import com.example.WebBanSach.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class PasswordResetController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserServices userService;

    @GetMapping("/reset-password")
    public String showResetPasswordForm() {
        return "user/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email) {
        // Check if the email is registered
        if (!userService.isEmailRegistered(email)) {
            // Redirect with error message that email is not registered
            return "redirect:/reset-password?error=emailNotRegistered";
        }

        try {
            // Generate OTP
            String otp = generateOTP();

            // Save OTP to database with email and expiry time (1 hour)
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(otp);
            resetToken.setEmail(email);
            resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
            tokenRepository.save(resetToken);

            // Send OTP via email
            String subject = "Password Reset OTP";
            String body = "Your OTP for password reset is: " + otp;
            emailService.sendSimpleEmail(email, subject, body);

            // Redirect to OTP verification page
            return "redirect:/change-password?token=" + resetToken.getToken();
        } catch (MessagingException e) {
            // Handle email sending failure
            return "redirect:/reset-password?error=email";
        }
    }

    // Helper method to generate a 6-digit OTP
    private String generateOTP() {
        int otpLength = 6;
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append((int) (Math.random() * 10));
        }
        return otp.toString();
    }

    @GetMapping("/change-password")
    public ModelAndView showChangePasswordForm(@RequestParam String token) {
        ModelAndView modelAndView = new ModelAndView("user/change-password");
        modelAndView.addObject("token", token);
        modelAndView.addObject("otp", ""); // Initialize OTP field
        return modelAndView;
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String token, @RequestParam String password, @RequestParam String otp) {
        // Check if token is valid and not expired
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isPresent() && optionalToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            PasswordResetToken resetToken = optionalToken.get();
            if (resetToken.getToken().equals(otp)) {
                userService.updatePassword(resetToken.getEmail(), password);
                tokenRepository.delete(resetToken);
                return "redirect:/login?passwordChanged";
            } else {
                return "redirect:/change-password?token=" + token + "&error=invalidOTP";
            }
        } else {
            return "redirect:/change-password?token=" + token + "&error=invalidToken";
        }
    }
}
