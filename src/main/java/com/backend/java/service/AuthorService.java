package com.backend.java.service;

import com.backend.java.domain.model.AuthorCreateRequestDTO;

public interface AuthorService {
    void createNewAuthor(AuthorCreateRequestDTO dto);
}
