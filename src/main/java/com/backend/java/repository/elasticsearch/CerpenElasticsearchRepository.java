package com.backend.java.repository.elasticsearch;

import com.backend.java.domain.document.CerpenIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CerpenElasticsearchRepository extends ElasticsearchRepository<CerpenIndex, UUID> {
    @Query("""
            {
                "terms": {
                    "_id": ?0
                }
            }
            """)
    Page<CerpenIndex> findDocumentsByIds(List<String> ids, Pageable pageable);
}
