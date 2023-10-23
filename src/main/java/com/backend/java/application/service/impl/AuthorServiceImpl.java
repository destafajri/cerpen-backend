package com.backend.java.application.service.impl;

import com.backend.java.application.dto.AuthorCreateRequestDTO;
import com.backend.java.application.exception.ValidationService;
import com.backend.java.application.service.AuthorService;
import com.backend.java.domain.entities.AuthorEntity;
import com.backend.java.domain.entities.UserEntity;
import com.backend.java.domain.model.RoleEnum;
import com.backend.java.repository.postgres.AuthorRepository;
import com.backend.java.repository.postgres.UserRepository;
import com.backend.java.utility.CurrentTimeStamp;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ValidationService validationService;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public void createNewAuthor(AuthorCreateRequestDTO payload) {
        validationService.validate(payload);

        UserEntity user = new UserEntity();
        AuthorEntity author = new AuthorEntity();

        user.setEmail(payload.getEmail());
        user.setUsername(payload.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(payload.getPassword()));
        user.setRole(RoleEnum.AUTHOR);
        user.setActive(true);
        user.setCreatedAt(CurrentTimeStamp.getLocalDateTime());
        author.setUser(user);
        author.setName(payload.getName());

        userRepository.save(user);
        authorRepository.save(author);
    }
}
