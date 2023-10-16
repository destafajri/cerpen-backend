package com.backend.java.application.listeners;

import com.backend.java.application.event.CerpenEntityEvent;
import com.backend.java.domain.document.CerpenIndex;
import com.backend.java.domain.entities.CerpenEntity;
import com.backend.java.repository.elasticsearch.CerpenElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CerpenElasticsearchEventListener {

    @Autowired
    private final CerpenElasticsearchRepository elasticsearchRepository;

    @EventListener
    public void handleNewDataEvent(CerpenEntityEvent event) {
        try {
            // Get the newly created data from the event
            var newDataCerpen = event.getCerpenEntity();

            // ConvertUtils the NewData to CerpenIndex
            CerpenIndex cerpenIndex = convertToCerpenIndex(newDataCerpen);

            // Save the new data to Elasticsearch using the repository
            elasticsearchRepository.save(cerpenIndex);

            log.info("Even Created....");
        } catch (Exception e) {
            e.printStackTrace();
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error when insert into elasticsearch");
        }
    }

    private CerpenIndex convertToCerpenIndex(CerpenEntity newData) {
        CerpenIndex cerpenIndex = new CerpenIndex();
        cerpenIndex.setId(newData.getId());
        cerpenIndex.setAuthorId(newData.getAuthor().getId());
        cerpenIndex.setAuthorName(newData.getAuthor().getName());
        cerpenIndex.setTitle(newData.getTitle());
        cerpenIndex.setTema(newData.getTema());
        cerpenIndex.setCerpenContains(newData.getCerpenContains());
        cerpenIndex.setCreatedAt(newData.getCreatedAt());
        cerpenIndex.setUpdatedAt(newData.getUpdatedAt());

        return cerpenIndex;
    }
}

