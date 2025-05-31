package com.example.demo.config;

import org.springframework.stereotype.Component;

@Component
public class DataSourceSelector {

    private static final String WRITE = "write";
    private static final String READ = "read";

    private String selected = WRITE;

    public void toWrite() {
        this.selected = WRITE;
    }

    public void toRead() {
        this.selected = READ;
    }

    public String getSelected() {
        return selected;
    }
}