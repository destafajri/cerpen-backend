package com.backend.java.service.impl;

import com.backend.java.domain.entities.AuthorEntity;
import com.backend.java.domain.entities.CerpenEntity;
import com.backend.java.domain.model.CerpenCreateRequestDTO;
import com.backend.java.exception.ValidationService;
import com.backend.java.repository.CerpenRepository;
import com.backend.java.repository.postgres.AuthorRepository;
import com.backend.java.service.CerpenService;
import com.backend.java.utility.CurrentTimeStamp;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CerpenServiceImpl implements CerpenService {

    @Autowired
    private ValidationService validationService;
    private final CerpenRepository cerpenRepository;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public void createNewCerpen(String username, CerpenCreateRequestDTO dto) {
        validationService.validate(dto);

        CerpenEntity cerpenEntity = new CerpenEntity();
        AuthorEntity author = authorRepository.findAuthorByUsername(username);

        cerpenEntity.setAuthorId(author);
        cerpenEntity.setTitle(dto.getTitle());
        cerpenEntity.setTema(dto.getTema());
        cerpenEntity.setCerpenContains(dto.getCerpenContains());
        cerpenEntity.setCreatedAt(CurrentTimeStamp.getLocalDateTime());

        cerpenRepository.save(cerpenEntity);
    }
}
