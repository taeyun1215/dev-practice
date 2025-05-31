package com.example.demo.config;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Statement;
import java.util.stream.Collectors;

// DatabaseInitializer, DatabaseCleaner 둘중에 하나만 있으면 됨
@Component
public class DatabaseInitializer implements InitializingBean {

    @Autowired
    private DataSourceSelector dataSourceSelector;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SchemaGenerator schemaGenerator;

    @Override
    public void afterPropertiesSet() {
        initializeDatabases();
    }

    private void initializeDatabases() {
        // Write DB 초기화 (항상 깨끗한 상태 유지)
        dataSourceSelector.toWrite();
        executeDDL(schemaGenerator.generateDropDDL());
        executeDDL(schemaGenerator.generateCreateDDL());

        // Read DB 초기화 + Fixture 삽입
        dataSourceSelector.toRead();
        executeDDL(schemaGenerator.generateCreateDDL());
        loadFixtures("db/read_db_fixtures.sql");

        // 기본 상태 복구 (Write 모드로 설정)
        dataSourceSelector.toWrite();
    }

    private void executeDDL(String ddl) {
        entityManager.unwrap(Session.class).doWork(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(ddl);
            }
        });
    }

    private void loadFixtures(String filePath) {
        entityManager.unwrap(Session.class).doWork(connection -> {
            try (Statement statement = connection.createStatement();
                 InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String sql = reader.lines().collect(Collectors.joining("\n"));
                statement.execute(sql);

            } catch (Exception e) {
                throw new RuntimeException("Error executing SQL file: " + filePath, e);
            }
        });
    }
}