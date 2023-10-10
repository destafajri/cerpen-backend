package com.backend.java.application.service.impl;

import com.backend.java.application.dto.CerpenCreateRequestDTO;
import com.backend.java.application.event.CerpenCreatedEvent;
import com.backend.java.application.exception.ValidationService;
import com.backend.java.application.service.CerpenService;
import com.backend.java.domain.entities.AuthorEntity;
import com.backend.java.domain.entities.CerpenEntity;
import com.backend.java.repository.postgres.AuthorRepository;
import com.backend.java.repository.postgres.CerpenRepository;
import com.backend.java.utility.CurrentTimeStamp;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CerpenServiceImpl implements CerpenService {

    @Autowired
    private ValidationService validationService;
    private final CerpenRepository cerpenRepository;
    private final AuthorRepository authorRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void createNewCerpen(String username, CerpenCreateRequestDTO dto) {
        validationService.validate(dto);

        CerpenEntity cerpenEntity = new CerpenEntity();
        AuthorEntity author = authorRepository.findAuthorByUsername(username);

        cerpenEntity.setAuthor(author);
        cerpenEntity.setTitle(dto.getTitle());
        cerpenEntity.setTema(dto.getTema());
        cerpenEntity.setCerpenContains(dto.getCerpenContains());
        cerpenEntity.setCreatedAt(CurrentTimeStamp.getLocalDateTime());

        cerpenRepository.save(cerpenEntity);

        // Publish the custom event
        eventPublisher.publishEvent(new CerpenCreatedEvent(this, cerpenEntity));
    }
}
