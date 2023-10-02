package com.backend.java.service;

import com.backend.java.domain.model.CerpenCreateRequestDTO;

public interface CerpenService {

    void createNewCerpen(String username, CerpenCreateRequestDTO dto);
}
