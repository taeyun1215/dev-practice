package com.example.demo;

import io.debezium.config.Configuration;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DatabaseChangeCaptureService {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostConstruct
    public void start() {
        Configuration config = Configuration.create()
                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
                .with("database.hostname", "localhost")
                .with("database.port", "3306")
                .with("database.user", "root")
                .with("database.password", "password")
                .with("database.server.id", "184054")
                .with("database.server.name", "dbserver1")
                .with("database.whitelist", "test-db")
                .with("database.history.kafka.bootstrap.servers", "localhost:9092")
                .with("database.history.kafka.topic", "schema-changes.test-db")
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            DebeziumEngine engine = DebeziumEngine.create(Json.class)
                    .using(config)
                    .notifying(this::handleEvent)
                    .build();
            engine.run();
        });
    }

    private void handleEvent(SourceRecord record) {
        // Handle database event, transform it and send to Kafka
        kafkaProducerService.send(record);
    }
}
