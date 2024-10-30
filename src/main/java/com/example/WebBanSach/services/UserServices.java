package com.example.WebBanSach.services;

import com.example.WebBanSach.entity.Order;
import com.example.WebBanSach.entity.Role;
import com.example.WebBanSach.entity.User;
import com.example.WebBanSach.repository.IUserRepository;
import com.example.WebBanSach.repository.OrderRepository;
import com.example.WebBanSach.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServices {

    private final IUserRepository userRepository;
    private final RoleRepository roleRepository;


    @Autowired
    public UserServices(IUserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        createDefaultRoles();
    }

    @Transactional
    public void save(User user) {
        //user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            throw new RuntimeException("Role not found: USER");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Transactional
    public void createAdminUser() {
        if (userRepository.findByUsername("admin") == null) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(new BCryptPasswordEncoder().encode("123"));
            adminUser.setName("Admin User");

            Role adminRole = roleRepository.findByName("ADMIN");
            if (adminRole == null) {
                throw new RuntimeException("Role not found: ADMIN");
            }
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminUser.setRoles(adminRoles);
            userRepository.save(adminUser);
        }
    }

    @Transactional
    private void createDefaultRoles() {
        if (roleRepository.findByName("USER") == null) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName("ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.save(user);
        }
    }
    public User getUserById(Long id) {
        return userRepository.findById(Math.toIntExact(id)).orElse(null);
    }
    public boolean isEmailRegistered(String email) {
        // Implement logic to check if email exists in UserRepository
        return userRepository.findByEmail(email) != null;
    }


    public void lockUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresent(user -> {
            user.setEnabled(false);
            userRepository.save(user);
        });
    }

    public void unlockUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresent(user -> {
            user.setEnabled(true);
            userRepository.save(user);
        });
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
