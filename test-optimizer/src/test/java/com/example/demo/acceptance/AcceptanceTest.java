package com.example.demo.acceptance;

import com.example.demo.config.DataSourceSelector;
import com.example.demo.config.DatabaseCleaner;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AcceptanceTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected DataSourceSelector dataSourceSelector;

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        dataSourceSelector.toWrite();
        databaseCleaner.clear();
    }
}