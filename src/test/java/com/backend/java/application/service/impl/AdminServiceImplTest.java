package com.backend.java.application.service.impl;

import com.backend.java.application.dto.AdminCreateRequestDTO;
import com.backend.java.application.exception.ValidationService;
import com.backend.java.domain.entities.AdminEntity;
import com.backend.java.domain.entities.UserEntity;
import com.backend.java.domain.model.RoleEnum;
import com.backend.java.repository.postgres.AdminRepository;
import com.backend.java.repository.postgres.UserRepository;
import com.backend.java.utility.CurrentTimeStamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class AdminServiceImplTest {
    @InjectMocks
    private AdminServiceImpl adminService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private ValidationService validationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdminRepository adminRepository;
    private UserEntity userEntity;
    private AdminEntity adminEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.userEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .username("testAdminr")
                .email("admintest@yahoo.com")
                .password("active")
                .role(RoleEnum.ADMIN)
                .isActive(true)
                .createdAt(CurrentTimeStamp.getLocalDateTime())
                .build();
        this.adminEntity = AdminEntity.builder()
                .id(UUID.randomUUID())
                .user(userEntity)
                .name("test Unit")
                .build();
    }

    @Test
    public void createNewAdmin() {
        // Arr
        AdminCreateRequestDTO dto = new AdminCreateRequestDTO();
        dto.setEmail("test@example.com");
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setName("Test Admin");

        // Mock
        UserEntity mockUser = UserEntity.builder()
                .id(UUID.randomUUID())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password("hashedPassword")
                .role(RoleEnum.ADMIN)
                .isActive(true)
                .createdAt(CurrentTimeStamp.getLocalDateTime())
                .build();
        AdminEntity mockAdmin = AdminEntity.builder()
                .id(UUID.randomUUID())
                .user(mockUser)
                .name(dto.getName())
                .build();

        when(bCryptPasswordEncoder.encode(dto.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);
        when(adminRepository.save(any(AdminEntity.class))).thenReturn(mockAdmin);

        // Acc
        adminService.createNewAdmin(dto);

        // Verify
        verify(validationService, times(1)).validate(dto);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(adminRepository, times(1)).save(any(AdminEntity.class));
    }
}