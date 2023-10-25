package com.backend.java.application.service.impl;

import com.backend.java.application.dto.CerpenCreateRequestDTO;
import com.backend.java.application.dto.CerpenListByIdRequestDTO;
import com.backend.java.application.dto.CerpenResponseDTO;
import com.backend.java.application.dto.UpdateCerpenDTO;
import com.backend.java.application.event.CerpenEntityEvent;
import com.backend.java.application.exception.ValidationService;
import com.backend.java.domain.document.CerpenIndex;
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
import com.backend.java.utility.PaginationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    private UserEntity userAdminEntity;
    private AdminEntity adminEntity;
    private AuthorEntity authorEntity;
    private CerpenEntity cerpenEntity;
    private CerpenIndex cerpenIndex;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.userAdminEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .username("testAdmin")
                .email("admintest@yahoo.com")
                .password("active")
                .role(RoleEnum.ADMIN)
                .isActive(true)
                .createdAt(CurrentTimeStamp.getLocalDateTime())
                .build();
        this.adminEntity = AdminEntity.builder()
                .id(UUID.randomUUID())
                .user(userAdminEntity)
                .name("test Unit admin")
                .build();
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
                .isActive(true)
                .createdAt(CurrentTimeStamp.getLocalDateTime())
                .updatedAt(CurrentTimeStamp.getLocalDateTime())
                .build();
        this.cerpenIndex = CerpenIndex.builder()
                .id(this.cerpenEntity.getId())
                .authorId(this.authorEntity.getId())
                .authorName(this.authorEntity.getName())
                .title(this.cerpenEntity.getTitle())
                .tema(this.cerpenEntity.getTema())
                .cerpenContains(this.cerpenEntity.getCerpenContains())
                .isActive(this.cerpenEntity.getIsActive())
                .createdAt(this.cerpenEntity.getCreatedAt())
                .updatedAt(this.cerpenEntity.getUpdatedAt())
                .build();
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
        Assertions.assertEquals(cerpenEntity.getIsActive(), true);

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

    @Test
    public void searchCerpen() {
        // Arrange
        String keyword = "test";
        Integer pageNumber = 0;
        Integer limit = 10;
        String sortBy = "_score";
        String sortOrder = "desc";

        Pageable pageable = PaginationUtils.createPageable(pageNumber, limit, sortBy, sortOrder);

        // Mock the behavior of cerpenElasticsearchRepository searchCerpen method
        List<CerpenIndex> mockSearchResults = Collections.singletonList(cerpenIndex);
        Page<CerpenIndex> mockPage = new PageImpl<>(mockSearchResults);
        Mockito.when(cerpenElasticsearchRepository.searchCerpen(keyword, pageable)).thenReturn(mockPage);

        // Action
        List<CerpenResponseDTO> result = cerpenService.searchCerpen(keyword, pageNumber, limit, sortBy, sortOrder);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get(0).getId(), cerpenIndex.getId());
        Assertions.assertEquals(result.get(0).getId(), cerpenEntity.getId());
        Assertions.assertEquals(result.get(0).getAuthorName(), cerpenEntity.getAuthor().getName());
        Assertions.assertEquals(result.get(0).getCerpenContains(), cerpenIndex.getCerpenContains());

        // Verify
        verify(cerpenElasticsearchRepository, Mockito.times(1))
                .searchCerpen(keyword, pageable);
    }

    @Test
    public void getListCerpenById() {
        // Arrange
        CerpenListByIdRequestDTO dto = new CerpenListByIdRequestDTO();
        dto.setId(List.of(this.cerpenEntity.getId(), UUID.randomUUID()));
        Integer pageNumber = 0;
        Integer limit = 10;
        String sortBy = "_score";
        String sortOrder = "asc";

        Pageable pageable = PaginationUtils.createPageable(pageNumber, limit, sortBy, sortOrder);

        // Mock the behavior of cerpenRepository findCerpenAndAuthorNamesByIds method
        List<CerpenEntity> mockCerpenData = List.of(
                this.cerpenEntity,
                CerpenEntity.builder()
                        .id(dto.getId().get(1))
                        .author(this.authorEntity)
                        .title("Test Judul 2")
                        .tema(TemaEnum.HORROR)
                        .cerpenContains("Test Contains 2")
                        .isActive(true)
                        .createdAt(CurrentTimeStamp.getLocalDateTime())
                        .updatedAt(CurrentTimeStamp.getLocalDateTime())
                        .build()
        );

        Page<CerpenEntity> mockPage = new PageImpl<>(mockCerpenData);
        Mockito.when(cerpenRepository.findCerpenAndAuthorNamesByIds(dto.getId(), pageable)).thenReturn(mockPage);

        // Act
        List<CerpenResponseDTO> result = cerpenService.getListCerpenById(dto, pageNumber, limit, sortBy, sortOrder);

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(dto.getId().get(1), result.get(1).getId());
        Assertions.assertEquals("Test Contains 1", result.get(0).getCerpenContains());
        Assertions.assertEquals("Test Contains 2", result.get(1).getCerpenContains());
        Assertions.assertEquals(cerpenEntity.getTema(), result.get(0).getTema());
        Assertions.assertNotEquals(cerpenEntity.getTema(), result.get(1).getTema());

        // Verify
        verify(cerpenRepository, Mockito.times(1))
                .findCerpenAndAuthorNamesByIds(dto.getId(), pageable);
    }

    @Test
    public void getDetailCerpen() {
        // Arrange
        UUID id = cerpenEntity.getId(); // Replace with a valid UUID for your test

        // mocking data
        when(cerpenElasticsearchRepository.findById(eq(id))).thenReturn(Optional.of(this.cerpenIndex));

        // Act
        CerpenResponseDTO result = cerpenService.getDetailCerpen(id);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(this.cerpenIndex.getId(), result.getId());

        // Verify
        verify(cerpenElasticsearchRepository, Mockito.times(1))
                .findById(id);

        // Another Assert
        Assertions.assertDoesNotThrow(() -> cerpenService.getDetailCerpen(cerpenEntity.getId()));

        // Verify
        verify(cerpenElasticsearchRepository, Mockito.times(2))
                .findById(id);
    }

    @Test
    public void getDetailCerpenNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(cerpenElasticsearchRepository.findById(eq(id))).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(ResponseStatusException.class, () -> cerpenService.getDetailCerpen(id));

        // Verify
        verify(cerpenElasticsearchRepository, Mockito.times(1))
                .findById(id);
    }

    @Test
    public void updateCerpenSuccess() {
        // Arrange
        String username = "testAuthor";
        UUID cerpenId = UUID.randomUUID();
        UpdateCerpenDTO dto = new UpdateCerpenDTO();
        dto.setTitle("New Title");
        dto.setTema(TemaEnum.DRAMA);
        dto.setCerpenContains("New Contents");

        // mocking data
        AuthorEntity mockAuthor = this.authorEntity;
        CerpenEntity mockCerpenEntity = CerpenEntity.builder()
                .id(cerpenId)
                .author(mockAuthor)
                .title(dto.getTitle())
                .tema(dto.getTema())
                .cerpenContains(dto.getCerpenContains())
                .isActive(true)
                .createdAt(CurrentTimeStamp.getLocalDateTime())
                .updatedAt(CurrentTimeStamp.getLocalDateTime())
                .build();

        Mockito.doNothing().when(validationService).validate(dto);
        Mockito.when(authorRepository.findAuthorByUsername(username)).thenReturn(mockAuthor);
        Mockito.when(cerpenRepository.findById(eq(cerpenId))).thenReturn(Optional.of(mockCerpenEntity));

        // Act and Assert
        Assertions.assertDoesNotThrow(() -> cerpenService.updateCerpen(username, cerpenId, dto));

        // Verify
        verify(validationService, Mockito.times(1)).validate(dto);
        verify(authorRepository, Mockito.times(1)).findAuthorByUsername("testAuthor");
        verify(cerpenRepository, Mockito.times(1)).save(any(CerpenEntity.class));
        verify(eventPublisher, Mockito.times(1)).publishEvent(any(CerpenEntityEvent.class));
    }

    @Test
    public void updateCerpenNotFound() {
        // Arrange
        String username = "testAuthor";
        UUID cerpenId = UUID.randomUUID();
        UpdateCerpenDTO dto = new UpdateCerpenDTO();

        Mockito.doNothing().when(validationService).validate(dto);
        when(authorRepository.findAuthorByUsername(username)).thenReturn(new AuthorEntity());
        when(cerpenRepository.findById(eq(cerpenId))).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(ResponseStatusException.class, () -> cerpenService.updateCerpen(username, cerpenId, dto));

        // Verify
        verify(authorRepository, Mockito.times(1)).findAuthorByUsername("testAuthor");
    }

    @Test
    public void updateCerpenUserNotMatch() {
        // Arrange
        String username = "testAuthorNotMatch";
        UUID cerpenId = UUID.randomUUID();
        UpdateCerpenDTO dto = new UpdateCerpenDTO();
        dto.setTitle("New Title");
        dto.setTema(TemaEnum.DRAMA);
        dto.setCerpenContains("New Contents");

        // mocking data
        AuthorEntity mockAuthor = AuthorEntity.builder()
                .id(UUID.randomUUID())
                .user(UserEntity.builder()
                        .username("testAuthorNotMatch")
                        .email("authortest2@yahoo.com")
                        .password("active")
                        .role(RoleEnum.AUTHOR)
                        .isActive(true)
                        .createdAt(CurrentTimeStamp.getLocalDateTime())
                        .build())
                .name("heheheh")
                .build();
        CerpenEntity mockCerpenEntity = CerpenEntity.builder()
                .id(cerpenId)
                .author(this.authorEntity)
                .title(dto.getTitle())
                .tema(dto.getTema())
                .cerpenContains(dto.getCerpenContains())
                .isActive(true)
                .createdAt(CurrentTimeStamp.getLocalDateTime())
                .updatedAt(CurrentTimeStamp.getLocalDateTime())
                .build();

        Mockito.doNothing().when(validationService).validate(dto);
        Mockito.when(authorRepository.findAuthorByUsername(username)).thenReturn(mockAuthor);
        Mockito.when(authorRepository.findAuthorByUsername("testAuthor")).thenReturn(this.authorEntity);
        Mockito.when(cerpenRepository.findById(eq(cerpenId))).thenReturn(Optional.of(mockCerpenEntity));

        // act and assertion
        Assertions.assertThrows(ResponseStatusException.class,
                () -> cerpenService.updateCerpen(username, cerpenId, dto)
        );

        // Verify
        verify(validationService, Mockito.times(1)).validate(dto);
        verify(authorRepository, Mockito.times(1)).findAuthorByUsername(username);
        verify(cerpenRepository, Mockito.times(0)).save(any(CerpenEntity.class));
        verify(eventPublisher, Mockito.times(0)).publishEvent(any(CerpenEntityEvent.class));
    }

    @Test
    public void activateCerpen() {
        // arrange
        var cerpenId = UUID.randomUUID();

        // mockdata
        CerpenEntity mockCerpen = CerpenEntity.builder()
                .id(cerpenId)
                .author(this.authorEntity)
                .title(this.cerpenEntity.getTitle())
                .tema(this.cerpenEntity.getTema())
                .cerpenContains(this.cerpenEntity.getCerpenContains())
                .isActive(false)
                .createdAt(CurrentTimeStamp.getLocalDateTime())
                .updatedAt(CurrentTimeStamp.getLocalDateTime())
                .build();

        when(cerpenRepository.findById(cerpenId)).thenReturn(Optional.of(mockCerpen));

        // Act
        cerpenService.activateCerpen(cerpenId);

        // Assertion
        Assertions.assertTrue(mockCerpen.getIsActive());

        // Verify
        verify(cerpenRepository, Mockito.times(1)).save(mockCerpen);
        verify(eventPublisher, Mockito.times(1)).publishEvent(any(CerpenEntityEvent.class));
    }

    @Test
    public void activateCerpenNotFound() {
        // arr
        var cerpenId = UUID.randomUUID();

        // Mock the behavior of the repository to return an empty Optional when findById is called
        Mockito.when(cerpenRepository.findById(cerpenId)).thenReturn(Optional.empty());

        // Verify that calling activateCerpen with an unknown cerpenId results in a ResponseStatusException
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> cerpenService.activateCerpen(cerpenId)
        );
        // another verify
        verify(cerpenRepository, Mockito.times(0)).save(any(CerpenEntity.class));
        verify(eventPublisher, Mockito.times(0)).publishEvent(any(CerpenEntityEvent.class));

        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("Cerpen Not Found", exception.getReason());
    }

    @Test
    public void deactivateCerpen() {
        // arrange
        var cerpenId = UUID.randomUUID();

        // mockdata
        CerpenEntity mockCerpen = CerpenEntity.builder()
                .id(cerpenId)
                .author(this.authorEntity)
                .title(this.cerpenEntity.getTitle())
                .tema(this.cerpenEntity.getTema())
                .cerpenContains(this.cerpenEntity.getCerpenContains())
                .isActive(true)
                .createdAt(CurrentTimeStamp.getLocalDateTime())
                .updatedAt(CurrentTimeStamp.getLocalDateTime())
                .build();

        when(cerpenRepository.findById(cerpenId)).thenReturn(Optional.of(mockCerpen));

        // Act
        cerpenService.deactivateCerpen(cerpenId);

        // Assertion
        Assertions.assertFalse(mockCerpen.getIsActive());

        // Verify
        verify(cerpenRepository, Mockito.times(1)).save(mockCerpen);
        verify(eventPublisher, Mockito.times(1)).publishEvent(any(CerpenEntityEvent.class));
    }

    @Test
    public void deactivateCerpenNotFound() {
        // arr
        var cerpenId = UUID.randomUUID();

        // Mock the behavior of the repository to return an empty Optional when findById is called
        Mockito.when(cerpenRepository.findById(cerpenId)).thenReturn(Optional.empty());

        // Verify that calling deactivateCerpen with an unknown cerpenId results in a ResponseStatusException
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> cerpenService.deactivateCerpen(cerpenId)
        );
        // Another verify
        verify(cerpenRepository, Mockito.times(0)).save(any(CerpenEntity.class));
        verify(eventPublisher, Mockito.times(0)).publishEvent(any(CerpenEntityEvent.class));

        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("Cerpen Not Found", exception.getReason());
    }
}
