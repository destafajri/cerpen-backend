package com.backend.java.domain.entities;

import com.backend.java.domain.model.TemaEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "cerpens")
public class CerpenEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "com.backend.java.utility.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorEntity authorId;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "tema", nullable = false)
    private TemaEnum tema;

    @Column(name = "cerpen_contains", length = 1000, nullable = false, unique = true)
    private String cerpenContains;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}

