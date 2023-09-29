package com.backend.java.service.impl;

import com.backend.java.domain.entities.AdminEntity;
import com.backend.java.domain.entities.UserEntity;
import com.backend.java.domain.model.AdminCreateRequestDTO;
import com.backend.java.repository.postgres.AdminRepository;
import com.backend.java.repository.postgres.UserRepository;
import com.backend.java.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public void createNewAdmin(AdminCreateRequestDTO payload) {
        UserEntity user = new UserEntity();
        AdminEntity admin = new AdminEntity();

        user.setEmail(payload.getEmail());
        user.setUsername(payload.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(payload.getPassword()));
        user.setRole("admin");
        user.setActive(true);
        user.setCreatedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
        admin.setUserId(user);
        admin.setName(payload.getName());

        userRepository.save(user);
        adminRepository.save(admin);
    }
}
