package com.example.WebBanSach.Validtion;

import com.example.WebBanSach.entity.User;
import com.example.WebBanSach.repository.IUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidUserldValidator  implements ConstraintValidator<ValidUserId, User> {
    @Autowired
    private IUserRepository userRepository;


    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        if (userRepository == null  || user == null) {
            return true; // Trả về true nếu id null
        }
        // Kiểm tra xem id đã tồn tại trong cơ sở dữ liệu chưa
        return userRepository.findById(Math.toIntExact(user.getId())) == null;
    }
}