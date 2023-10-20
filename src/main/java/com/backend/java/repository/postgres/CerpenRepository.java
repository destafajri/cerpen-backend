package com.backend.java.repository.postgres;

import com.backend.java.domain.entities.CerpenEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CerpenRepository extends JpaRepository<CerpenEntity, UUID> {
    @Query(value = """
            select c.*, a.name from cerpens c 
                join authors a on c.author_id = a.id
                    where c.id IN (:ids)
            """, nativeQuery = true)
    Page<CerpenEntity> findCerpenAndAuthorNamesByIds(List<UUID> ids, Pageable pageable);
}