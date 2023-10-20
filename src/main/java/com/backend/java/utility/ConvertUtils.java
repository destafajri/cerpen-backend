package com.backend.java.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConvertUtils {
    public static ArrayList<String> UUIDListToStringList(List<UUID> dto) {
        return dto.stream()
                .map(UUID::toString)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
