package com.backend.java.domain.document;

import com.backend.java.domain.model.TemaEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Setting(settingPath = "static/es-setting/cerpens-setting.json")
@Mapping(mappingPath = "static/es-mapping/cerpens-mapping.json")
@Document(indexName = "cerpens")
public class CerpenIndex {
    @Id
    private UUID id;

    @Field(name = "author_id")
    private UUID authorId;

    @Field(type = FieldType.Text, name = "author_name")
    private String authorName;

    @Field(type = FieldType.Text, name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Field(type = FieldType.Keyword, name = "tema")
    private TemaEnum tema;

    @Field(type = FieldType.Text, name = "cerpen_contains")
    private String cerpenContains;

    @Field(type = FieldType.Date, name = "created_at")
    private Date createdAt;

    @Field(type = FieldType.Date, name = "updated_at")
    private Date updatedAt;
}
