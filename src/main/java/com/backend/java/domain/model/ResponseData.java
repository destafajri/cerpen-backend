package com.backend.java.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResponseData<T> {

    private Integer code;
    private HttpStatus status;
    private List<String> message;
    private T data;
    private String token;

    public List<String> getMessage() {
        return (message == null) ? (message = new ArrayList<>()) : message;
    }
}
