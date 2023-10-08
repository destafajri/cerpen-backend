package com.backend.java.utility;

import java.sql.Timestamp;

public class CurrentTimeStamp {
    public static Timestamp getLocalDateTime() {
        return Timestamp.valueOf(java.time.LocalDateTime.now());
    }
}
