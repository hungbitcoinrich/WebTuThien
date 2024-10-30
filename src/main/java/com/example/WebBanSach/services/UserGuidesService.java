package com.example.WebBanSach.services;

import com.example.WebBanSach.entity.OrderingMethods;
import com.example.WebBanSach.entity.UserGuides;
import com.example.WebBanSach.repository.UserGuidesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserGuidesService {
    @Autowired
    private UserGuidesRepository userGuidesRepository;

    public List<UserGuides> getAllUserGuides() {
        return userGuidesRepository.findAll();
    }

    public Optional<UserGuides> getUserGuidesById(Long id) {
        return userGuidesRepository.findById(id);
    }

    public void saveUserGuides(UserGuides userGuides) {
        userGuidesRepository.save(userGuides);
    }

    public void deleteUserGuides(Long id) {
        userGuidesRepository.deleteById(id);
    }

    public void updateUserGuides(UserGuides userGuides) {
        userGuidesRepository.save(userGuides);
    }
}
