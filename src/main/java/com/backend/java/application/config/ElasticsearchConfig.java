package com.backend.java.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.backend.java.repository.elasticsearch")
@ComponentScan(basePackages = {"com.backend.java"})
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    // elasticsearch host
    @Value("${elasticsearch.host}")
    private String HOST;
    // elasticsearch port
    @Value("${elasticsearch.port}")
    private String PORT;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder().connectedTo(HOST + ":" + PORT).build();
    }
}
