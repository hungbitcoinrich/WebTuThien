package com.example.WebBanSach.services;

import com.example.WebBanSach.entity.Policy;
import com.example.WebBanSach.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PolicyService {
    @Autowired
    private PolicyRepository policyRepository;

    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    public Optional<Policy> getPolicyById(Long id) {
        return policyRepository.findById(id);
    }

    public void savePolicy(Policy policy) {
        policyRepository.save(policy);
    }

    public void deletePolicy(Long id) {
        policyRepository.deleteById(id);
    }

    public void updatePolicy(Policy policy) {
        policyRepository.save(policy);
    }
}
