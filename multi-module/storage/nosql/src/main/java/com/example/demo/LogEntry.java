package com.example.demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "logs")
public class LogEntry {
    @Id
    private String id;
    private Date timestamp;
    private String level;
    private String message;
    private String source;

    // Constructors, Getters, Setters
    public LogEntry() {}

    public LogEntry(Date timestamp, String level, String message, String source) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.source = source;
    }

    // standard getters and setters
}

