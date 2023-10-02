package com.backend.java.repository;

import com.backend.java.domain.entities.CerpenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CerpenRepository extends JpaRepository<CerpenEntity, UUID> {
}