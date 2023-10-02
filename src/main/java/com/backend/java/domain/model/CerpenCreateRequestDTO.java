package com.backend.java.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class CerpenCreateRequestDTO {

    @NotBlank(message = "title must not be blank")
    private String title;
    @NotBlank(message = "cerpen contains must not be blank")
    private String cerpenContains;

}