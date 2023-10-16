package com.backend.java.application.dto;

import com.backend.java.domain.model.TemaEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Size;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class UpdateCerpenDTO {

    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters long")
    private String title;
    private TemaEnum tema;
    @Size(min = 10, max = 1000, message = "Cerpen must be between 10 and 1000 characters long")
    private String cerpenContains;
}
