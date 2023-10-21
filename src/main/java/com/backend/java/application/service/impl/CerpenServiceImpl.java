package com.backend.java.application.service.impl;

import com.backend.java.application.dto.CerpenCreateRequestDTO;
import com.backend.java.application.dto.CerpenListByIdRequestDTO;
import com.backend.java.application.dto.CerpenResponseDTO;
import com.backend.java.application.dto.UpdateCerpenDTO;
import com.backend.java.application.event.CerpenEntityEvent;
import com.backend.java.application.event.EventMethod;
import com.backend.java.application.exception.ValidationService;
import com.backend.java.application.service.CerpenService;
import com.backend.java.domain.document.CerpenIndex;
import com.backend.java.domain.entities.AuthorEntity;
import com.backend.java.domain.entities.CerpenEntity;
import com.backend.java.repository.elasticsearch.CerpenElasticsearchRepository;
import com.backend.java.repository.postgres.AuthorRepository;
import com.backend.java.repository.postgres.CerpenRepository;
import com.backend.java.utility.CurrentTimeStamp;
import com.backend.java.utility.PaginationUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@AllArgsConstructor
public class CerpenServiceImpl implements CerpenService {

    @Autowired
    private ValidationService validationService;
    private final CerpenRepository cerpenRepository;
    private final AuthorRepository authorRepository;
    private final CerpenElasticsearchRepository cerpenElasticsearchRepository;
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
        cerpenEntity.setIsActive(false);
        cerpenEntity.setCreatedAt(CurrentTimeStamp.getLocalDateTime());
        cerpenEntity.setUpdatedAt(CurrentTimeStamp.getLocalDateTime());

        cerpenRepository.save(cerpenEntity);

        // Publish the custom event
        eventPublisher.publishEvent(new CerpenEntityEvent(this, cerpenEntity, EventMethod.CREATE));
    }

    @Override
    @Transactional
    public List<CerpenResponseDTO> searchCerpen(String keyword,
                                                Integer pageNumber,
                                                Integer limit,
                                                String sortBy,
                                                String sortOrder) {
        var pagination = PaginationUtils.createPageable(pageNumber, limit, sortBy, sortOrder);
        var data = cerpenElasticsearchRepository.searchCerpen(keyword, pagination);

        log.info(data.toString());
        return StreamSupport.stream(data.spliterator(), false)
                .map(this::toCerpenResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CerpenResponseDTO> getListCerpenById(CerpenListByIdRequestDTO dto,
                                                     Integer pageNumber,
                                                     Integer limit,
                                                     String sortBy,
                                                     String sortOrder) {
        var pagination = PaginationUtils.createPageable(pageNumber, limit, sortBy, sortOrder);
        var data = cerpenRepository.findCerpenAndAuthorNamesByIds(dto.getId(), pagination);

        return StreamSupport.stream(data.spliterator(), false)
                .map(this::toCerpenResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CerpenResponseDTO getDetailCerpen(UUID id) {
        var data = cerpenElasticsearchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cerpen not found")
                );
        return toCerpenResponse(data);
    }

    @Override
    @Transactional
    public void updateCerpen(String username, UUID cerpenId, UpdateCerpenDTO dto) {
        validationService.validate(dto);

        AuthorEntity author = authorRepository.findAuthorByUsername(username);
        var cerpenEntity = cerpenRepository.findById(cerpenId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cerpen Not Found"));

        if (cerpenEntity.getAuthor() != (author)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not allowed to edit this cerpen");
        }

        cerpenEntity.setTitle(
                dto.getTitle() == null ? cerpenEntity.getTitle() : dto.getTitle());
        cerpenEntity.setTema(
                dto.getTema() == null ? cerpenEntity.getTema() : dto.getTema());
        cerpenEntity.setCerpenContains(
                dto.getCerpenContains() == null ? cerpenEntity.getCerpenContains() : dto.getCerpenContains());
        cerpenEntity.setUpdatedAt(CurrentTimeStamp.getLocalDateTime());

        cerpenRepository.save(cerpenEntity);

        // Publish the custom event
        eventPublisher.publishEvent(new CerpenEntityEvent(this, cerpenEntity, EventMethod.UPDATE));
    }

    @Override
    @Transactional
    public void deleteCerpen(String username, UUID cerpenId) {
        AuthorEntity author = authorRepository.findAuthorByUsername(username);
        var cerpenEntity = cerpenRepository.findById(cerpenId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cerpen Not Found"));

        if (cerpenEntity.getAuthor() != (author)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not allowed to edit this cerpen");
        }

        cerpenRepository.delete(cerpenEntity);
        eventPublisher.publishEvent(new CerpenEntityEvent(this, cerpenEntity, EventMethod.DELETE));
    }

    private CerpenResponseDTO toCerpenResponse(Object cerpen) {
        if (cerpen instanceof CerpenIndex) {
            return toCerpenResponseFromIndex((CerpenIndex) cerpen);
        }

        if (cerpen instanceof CerpenEntity) {
            return toCerpenResponseFromEntity((CerpenEntity) cerpen);
        }

        throw new IllegalArgumentException("Invalid cerpen type: " + cerpen.getClass().getName());
    }

    private CerpenResponseDTO toCerpenResponseFromIndex(CerpenIndex cerpenIndex) {
        return CerpenResponseDTO.builder()
                .id(cerpenIndex.getId())
                .authorName(cerpenIndex.getAuthorName())
                .title(cerpenIndex.getTitle())
                .tema(cerpenIndex.getTema())
                .cerpenContains(cerpenIndex.getCerpenContains())
                .isActive(cerpenIndex.getIsActive())
                .createdAt(cerpenIndex.getCreatedAt())
                .updatedAt(cerpenIndex.getUpdatedAt())
                .build();
    }

    private CerpenResponseDTO toCerpenResponseFromEntity(CerpenEntity cerpenEntity) {
        return CerpenResponseDTO.builder()
                .id(cerpenEntity.getId())
                .authorName(cerpenEntity.getAuthor().getName())
                .title(cerpenEntity.getTitle())
                .tema(cerpenEntity.getTema())
                .cerpenContains(cerpenEntity.getCerpenContains())
                .isActive(cerpenEntity.getIsActive())
                .createdAt(cerpenEntity.getCreatedAt())
                .updatedAt(cerpenEntity.getUpdatedAt())
                .build();
    }
}
