package com.backend.java.eventlistener.event;

import com.backend.java.domain.entities.CerpenEntity;
import org.springframework.context.ApplicationEvent;

public class CerpenCreatedEvent extends ApplicationEvent {

    private final CerpenEntity cerpenEntity;

    public CerpenCreatedEvent(Object source, CerpenEntity cerpenEntity) {
        super(source);
        this.cerpenEntity = cerpenEntity;
    }

    public CerpenEntity getCreatedCerpen() {
        return cerpenEntity;
    }
}
