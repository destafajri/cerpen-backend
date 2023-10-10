package com.backend.java.application.service;

import com.backend.java.application.dto.AdminCreateRequestDTO;

public interface AdminService {
    void createNewAdmin(AdminCreateRequestDTO dto);
}
