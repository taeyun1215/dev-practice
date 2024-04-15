package com.example.demo;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class SnowflakeIdGenerator {
    private static final long SEQUENCE_BITS = 12;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS;

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public synchronized long nextId() {
        long timestamp = getCurrentTimestamp();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;

        return timestamp + sequence;
    }

    private long getCurrentTimestamp() {
        String formatted = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);
        return Long.parseLong(formatted);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = getCurrentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentTimestamp();
        }
        return timestamp;
    }
}