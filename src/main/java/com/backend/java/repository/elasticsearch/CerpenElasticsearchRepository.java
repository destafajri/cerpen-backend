package com.backend.java.repository.elasticsearch;

import com.backend.java.domain.document.CerpenIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CerpenElasticsearchRepository extends ElasticsearchRepository<CerpenIndex, UUID> {

    @Query(query = """
            {
                "bool": {
                        "should": [
                            {
                                "match": {
                                    "title": "?0"
                                }
                            },
                            {
                                "match": {
                                    "author_name": "?0"
                                }
                            },
                            {
                                "match": {
                                    "tema": "?0"
                                }
                            },
                            {
                                "match": {
                                    "cerpen_contains": "?0"
                                }
                            }
                        ],
                        "filter": {
                            "match": {
                                "is_active": true
                            }
                        }
                    }
            }
            """)
    Page<CerpenIndex> searchCerpen(String keyword,
                                   Pageable pageable);
}
