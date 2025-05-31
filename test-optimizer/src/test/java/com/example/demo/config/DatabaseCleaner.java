package com.example.demo.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// DatabaseInitializer, DatabaseCleaner 둘중에 하나만 있으면 됨
@Component
public class DatabaseCleaner {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSourceSelector dataSourceSelector;

    private List<String> tableNames;

    @PostConstruct
    private void initialize() {
        tableNames = extractTableNames();
    }

    public void clear() {
        if (!dataSourceSelector.getSelected().equals("write")) {
            throw new IllegalStateException("DatabaseCleaner can only be used with WRITE database.");
        }

        entityManager.unwrap(Session.class).doWork(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("SET REFERENTIAL_INTEGRITY FALSE");
                for (String table : tableNames) {
                    statement.executeUpdate("TRUNCATE TABLE " + table);
                    statement.executeUpdate("ALTER TABLE " + table + " ALTER COLUMN id RESTART WITH 1");
                }
                statement.executeUpdate("SET REFERENTIAL_INTEGRITY TRUE");
            }
        });
    }

    private List<String> extractTableNames() {
        return entityManager.unwrap(Session.class).doReturningWork(connection -> {
            ResultSet tables = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
            List<String> tableNames = new ArrayList<>();
            while (tables.next()) {
                tableNames.add(tables.getString("TABLE_NAME"));
            }
            return tableNames;
        });
    }
}