package com.example.WebBanSach.services;

import com.example.WebBanSach.entity.CustomUserDetail;
import com.example.WebBanSach.entity.User;
import com.example.WebBanSach.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CustomUserDetailServices implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản với tên đăng nhập: " + username);
        }
        if (!user.isEnabled()) {
            throw new DisabledException("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ admin để được hỗ trợ.");
        }

        return new CustomUserDetail(user,userRepository); // Assuming CustomUserDetail extends UserDetails
    }
}
