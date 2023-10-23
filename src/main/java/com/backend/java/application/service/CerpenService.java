package com.backend.java.application.service;

import com.backend.java.application.dto.CerpenCreateRequestDTO;
import com.backend.java.application.dto.CerpenListByIdRequestDTO;
import com.backend.java.application.dto.CerpenResponseDTO;
import com.backend.java.application.dto.UpdateCerpenDTO;

import java.util.List;
import java.util.UUID;

public interface CerpenService {

    void createNewCerpen(String username, CerpenCreateRequestDTO dto);

    List<CerpenResponseDTO> searchCerpen(String keyword,
                                         Integer pageNumber,
                                         Integer limit,
                                         String sortBy,
                                         String sortOrder);

    List<CerpenResponseDTO> getListCerpenById(CerpenListByIdRequestDTO dto,
                                              Integer pageNumber,
                                              Integer limit,
                                              String sortBy,
                                              String sortOrder);

    CerpenResponseDTO getDetailCerpen(UUID id);

    void updateCerpen(String username, UUID cerpenId, UpdateCerpenDTO dto);

    void activateCerpen(UUID cerpenId);

    void deactivateCerpen(UUID cerpenId);

    void deleteCerpen(String username, UUID cerpenId);
}
