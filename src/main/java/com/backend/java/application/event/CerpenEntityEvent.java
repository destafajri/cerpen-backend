package com.backend.java.application.event;

import com.backend.java.domain.entities.CerpenEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class CerpenEntityEvent extends ApplicationEvent {

    @Getter
    private final CerpenEntity cerpenEntity;
    @Getter
    private final EventMethod eventMethod;

    public CerpenEntityEvent(Object source, CerpenEntity cerpenEntity, EventMethod eventMethod) {
        super(source);
        this.cerpenEntity = cerpenEntity;
        this.eventMethod = eventMethod;
    }
}
