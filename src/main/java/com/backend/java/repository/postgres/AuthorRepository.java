package com.backend.java.repository.postgres;

import com.backend.java.domain.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, UUID> {

    @Query(value = """
            select au.* from authors au 
                left join users u on au.user_id = u.id
                    where u.username = :username
            """, nativeQuery = true)
    AuthorEntity findAuthorByUsername(
            @Param("username") String username);
}