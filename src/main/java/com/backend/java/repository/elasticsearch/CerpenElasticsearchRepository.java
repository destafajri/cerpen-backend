package com.backend.java.repository.elasticsearch;

import com.backend.java.domain.document.CerpenIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CerpenElasticsearchRepository extends ElasticsearchRepository<CerpenIndex, UUID> {
}
