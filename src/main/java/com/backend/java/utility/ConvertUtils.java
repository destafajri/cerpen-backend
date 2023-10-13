package com.backend.java.utility;

import com.backend.java.application.dto.CerpenListByIdRequestDTO;

import java.util.ArrayList;
import java.util.UUID;

public class ConvertUtils {
    public static ArrayList<String> UUIDListToStringList(CerpenListByIdRequestDTO dto) {
        return dto.getId().stream()
                .map(UUID::toString)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
