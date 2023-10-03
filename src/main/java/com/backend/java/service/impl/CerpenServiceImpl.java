package com.backend.java.service.impl;

import com.backend.java.domain.entities.AuthorEntity;
import com.backend.java.domain.entities.CerpenEntity;
import com.backend.java.domain.model.CerpenCreateRequestDTO;
import com.backend.java.repository.CerpenRepository;
import com.backend.java.repository.postgres.AuthorRepository;
import com.backend.java.service.CerpenService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class CerpenServiceImpl implements CerpenService {

    private final CerpenRepository cerpenRepository;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public void createNewCerpen(String username, CerpenCreateRequestDTO dto) {
        CerpenEntity cerpenEntity = new CerpenEntity();
        AuthorEntity author = authorRepository.findAuthorByUsername(username);

        cerpenEntity.setAuthorId(author);
        cerpenEntity.setTitle(dto.getTitle());
        cerpenEntity.setTema(dto.getTema());
        cerpenEntity.setCerpenContains(dto.getCerpenContains());
        cerpenEntity.setCreatedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));

        cerpenRepository.save(cerpenEntity);
    }
}
