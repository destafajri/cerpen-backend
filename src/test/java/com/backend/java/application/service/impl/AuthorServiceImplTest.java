package com.backend.java.application.service.impl;

import com.backend.java.application.dto.AuthorCreateRequestDTO;
import com.backend.java.application.exception.ValidationService;
import com.backend.java.domain.entities.AuthorEntity;
import com.backend.java.domain.entities.UserEntity;
import com.backend.java.domain.model.RoleEnum;
import com.backend.java.repository.postgres.AuthorRepository;
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

public class AuthorServiceImplTest {

    @InjectMocks
    private AuthorServiceImpl authorService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private ValidationService validationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorRepository authorRepository;
    private UserEntity userEntity;
    private AuthorEntity authorEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.userEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .username("testAuthor")
                .email("authortest@yahoo.com")
                .password("active")
                .role(RoleEnum.AUTHOR)
                .isActive(true)
                .createdAt(CurrentTimeStamp.getLocalDateTime())
                .build();
        this.authorEntity = AuthorEntity.builder()
                .id(UUID.randomUUID())
                .user(userEntity)
                .name("test Unit")
                .build();
    }

    @Test
    public void createNewAuthor() {
        // Arr
        AuthorCreateRequestDTO dto = new AuthorCreateRequestDTO();
        dto.setEmail("test@example.com");
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setName("Test Author");

        // Mock
        UserEntity mockUser = UserEntity.builder()
                .id(UUID.randomUUID())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password("hashedPassword")
                .role(RoleEnum.AUTHOR)
                .isActive(true)
                .createdAt(CurrentTimeStamp.getLocalDateTime())
                .build();
        AuthorEntity mockAuthor = AuthorEntity.builder()
                .id(UUID.randomUUID())
                .user(mockUser)
                .name(dto.getName())
                .build();

        when(bCryptPasswordEncoder.encode(dto.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);
        when(authorRepository.save(any(AuthorEntity.class))).thenReturn(mockAuthor);

        // Acc
        authorService.createNewAuthor(dto);

        // Verify
        verify(validationService, times(1)).validate(dto);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(authorRepository, times(1)).save(any(AuthorEntity.class));
    }
}