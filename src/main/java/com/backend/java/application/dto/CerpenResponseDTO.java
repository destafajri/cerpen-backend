package com.backend.java.application.dto;

import com.backend.java.domain.model.TemaEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CerpenResponseDTO {

    private UUID id;
    private String authorName;
    private String title;
    private TemaEnum tema;
    private String cerpenContains;
    private Date createdAt;
    private Date updatedAt;
}
