package com.backend.java.domain.entities;

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

    @OneToOne
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorEntity authorId;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "cerpen_contains", nullable = false, unique = true)
    private String cerpenContains;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
