package com.backend.java.application.event;

import com.backend.java.domain.entities.CerpenEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class CerpenEntityEvent extends ApplicationEvent {

    @Getter
    private final CerpenEntity cerpenEntity;

    public CerpenEntityEvent(Object source, CerpenEntity cerpenEntity) {
        super(source);
        this.cerpenEntity = cerpenEntity;
    }
}
