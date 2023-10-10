package com.backend.java.application.service;

import com.backend.java.application.dto.AuthorCreateRequestDTO;

public interface AuthorService {
    void createNewAuthor(AuthorCreateRequestDTO dto);
}
