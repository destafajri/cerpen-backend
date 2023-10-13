package com.backend.java.application.service;

import com.backend.java.application.dto.CerpenCreateRequestDTO;
import com.backend.java.application.dto.CerpenListByIdRequestDTO;
import com.backend.java.application.dto.CerpenResponseDTO;

import java.util.List;

public interface CerpenService {

    void createNewCerpen(String username, CerpenCreateRequestDTO dto);

    List<CerpenResponseDTO> getListCerpenById(CerpenListByIdRequestDTO dto,
                                              Integer pageNumber,
                                              Integer limit,
                                              String sortBy,
                                              String sortOrder);
}
