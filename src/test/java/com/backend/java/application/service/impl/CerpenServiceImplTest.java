package com.backend.java.application.service.impl;

import com.backend.java.application.dto.CerpenCreateRequestDTO;
import com.backend.java.application.event.CerpenEntityEvent;
import com.backend.java.application.exception.ValidationService;
import com.backend.java.domain.entities.AdminEntity;
import com.backend.java.domain.entities.AuthorEntity;
import com.backend.java.domain.entities.CerpenEntity;
import com.backend.java.domain.entities.UserEntity;
import com.backend.java.domain.model.RoleEnum;
import com.backend.java.domain.model.TemaEnum;
import com.backend.java.repository.elasticsearch.CerpenElasticsearchRepository;
import com.backend.java.repository.postgres.AuthorRepository;
import com.backend.java.repository.postgres.CerpenRepository;
import com.backend.java.utility.CurrentTimeStamp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CerpenServiceImplTest {

    @InjectMocks
    private CerpenServiceImpl cerpenService;
    @Mock
    private ValidationService validationService;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private CerpenRepository cerpenRepository;
    @Mock
    private CerpenElasticsearchRepository cerpenElasticsearchRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    private UserEntity userAuthorEntity;
    private AdminEntity adminEntity;
    private AuthorEntity authorEntity;
    private CerpenEntity cerpenEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.userAuthorEntity = UserEntity.builder()
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
                .user(userAuthorEntity)
                .name("test Unit")
                .build();
        this.cerpenEntity = CerpenEntity.builder()
                .id(UUID.randomUUID())
                .author(this.authorEntity)
                .title("Test Judul 1")
                .tema(TemaEnum.DRAMA)
                .cerpenContains("Test Contains 1")
                .isActive(false)
                .createdAt(CurrentTimeStamp.getLocalDateTime())
                .updatedAt(CurrentTimeStamp.getLocalDateTime()).build();
    }

    @Test
    public void createNewCerpenSuccess() {
        // Arrange
        CerpenCreateRequestDTO createRequestDTO = new CerpenCreateRequestDTO();
        createRequestDTO.setTitle("Test Title");
        createRequestDTO.setTema(TemaEnum.DRAMA);
        createRequestDTO.setCerpenContains("Test Cerpen Contains");

        // mocking data
        when(authorRepository.findAuthorByUsername("testAuthor")).thenReturn(authorEntity);
        when(cerpenRepository.save(any(CerpenEntity.class))).thenReturn(new CerpenEntity(
                cerpenEntity.getId(),
                authorEntity,
                createRequestDTO.getTitle(),
                createRequestDTO.getTema(),
                createRequestDTO.getCerpenContains(),
                cerpenEntity.getIsActive(),
                cerpenEntity.getCreatedAt(),
                cerpenEntity.getUpdatedAt()
        ));

        // Action
        cerpenService.createNewCerpen("testAuthor", createRequestDTO);

        // Assert
        Assertions.assertNotNull(cerpenEntity);
        Assertions.assertEquals("testAuthor", userAuthorEntity.getUsername());
        Assertions.assertEquals(cerpenEntity.getAuthor().getUser().getRole(), RoleEnum.AUTHOR);
        Assertions.assertNotNull(cerpenEntity.getId());
        Assertions.assertEquals(cerpenEntity.getAuthor(), authorEntity);
        Assertions.assertEquals(cerpenEntity.getIsActive(), false);

        // Verify that the necessary methods were called
        verify(validationService).validate(createRequestDTO);
        verify(authorRepository).findAuthorByUsername("testAuthor");
        verify(cerpenRepository).save(any(CerpenEntity.class));
        verify(eventPublisher).publishEvent(any(CerpenEntityEvent.class));

        // verify that every method called one times
        verify(validationService, Mockito.times(1))
                .validate(createRequestDTO);
        verify(authorRepository, Mockito.times(1))
                .findAuthorByUsername("testAuthor");
        verify(cerpenRepository, Mockito.times(1))
                .save(any(CerpenEntity.class));
        verify(eventPublisher, Mockito.times(1))
                .publishEvent(any(CerpenEntityEvent.class));
    }
}
