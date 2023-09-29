package com.backend.java.service;

import com.backend.java.domain.model.AdminCreateRequestDTO;

public interface AdminService {
    void createNewAdmin(AdminCreateRequestDTO dto);
}
