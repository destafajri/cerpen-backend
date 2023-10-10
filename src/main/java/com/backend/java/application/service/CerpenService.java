package com.backend.java.application.service;

import com.backend.java.application.dto.CerpenCreateRequestDTO;

public interface CerpenService {

    void createNewCerpen(String username, CerpenCreateRequestDTO dto);
}
