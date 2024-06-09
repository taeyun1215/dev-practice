package com.example.demo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<LogEntry, String> {
    // 여기에 필요한 쿼리 메소드를 추가할 수 있습니다.
}