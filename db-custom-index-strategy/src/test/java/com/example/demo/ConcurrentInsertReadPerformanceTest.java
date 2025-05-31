package com.example.demo;

import com.example.demo.basic.UserBasic;
import com.example.demo.basic.UserBasicRepository;
import com.example.demo.ulid.UserULID;
import com.example.demo.ulid.UserULIDRepository;
import com.example.demo.uuid.UserUUID;
import com.example.demo.uuid.UserUUIDRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@SpringBootTest
public class ConcurrentInsertReadPerformanceTest {

    @Autowired
    private UserBasicRepository userBasicRepository;

    @Autowired
    private UserUUIDRepository userUUIDRepository;

    @Autowired
    private UserULIDRepository userULIDRepository;

    @Test
    public void testConcurrentInsertAndReadPerformanceForBasic() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 삽입 작업
        Runnable insertTask = () -> {
            for (int i = 0; i < 100000; i++) {
                userBasicRepository.save(
                        UserBasic.builder()
                                .email("email" + i)
                                .name("name" + i)
                                .build()
                );
            }
        };

        // 조회 작업 (시간 측정)
        Runnable readTask = () -> {
            List<Long> basicIds = new ArrayList<>();
            userBasicRepository.findAll().forEach(user -> basicIds.add(user.getId()));
            Collections.shuffle(basicIds);

            StopWatch stopWatch = new StopWatch();
            stopWatch.start("Basic Read Performance");
            basicIds.forEach(id -> userBasicRepository.findById(id));
            stopWatch.stop();

            System.out.println("Auto Concurrent Insert, Select: " + stopWatch.getTotalTimeSeconds() + " seconds");
            System.out.println(stopWatch.prettyPrint());
        };

        // 삽입 작업과 조회 작업 동시에 시작
        Future<?> insertFuture = executor.submit(insertTask);
        Future<?> readFuture = executor.submit(readTask);

        // 두 작업이 완료될 때까지 기다림
        insertFuture.get();
        readFuture.get();

        executor.shutdown();
    }

    @Test
    public void testConcurrentInsertAndReadPerformanceForUUID() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 삽입 작업
        Runnable insertTask = () -> {
            for (int i = 0; i < 100000; i++) {
                userUUIDRepository.save(
                        UserUUID.builder()
                                .email("email" + i)
                                .name("name" + i)
                                .build()
                );
            }
        };

        // 조회 작업 (시간 측정)
        Runnable readTask = () -> {
            List<UUID> uuidIds = new ArrayList<>();
            userUUIDRepository.findAll().forEach(user -> uuidIds.add(user.getId()));
            Collections.shuffle(uuidIds);

            StopWatch stopWatch = new StopWatch();
            stopWatch.start("UUID Read Performance");
            uuidIds.forEach(id -> userUUIDRepository.findById(id));
            stopWatch.stop();

            System.out.println("UUID Concurrent Insert, Select: " + stopWatch.getTotalTimeSeconds() + " seconds");
            System.out.println(stopWatch.prettyPrint());
        };

        // 삽입 작업과 조회 작업 동시에 시작
        Future<?> insertFuture = executor.submit(insertTask);
        Future<?> readFuture = executor.submit(readTask);

        // 두 작업이 완료될 때까지 기다림
        insertFuture.get();
        readFuture.get();

        executor.shutdown();
    }

    @Test
    public void testConcurrentInsertAndReadPerformanceForULID() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 삽입 작업
        Runnable insertTask = () -> {
            for (int i = 0; i < 100000; i++) {
                userULIDRepository.save(
                        UserULID.builder()
                                .email("email" + i)
                                .name("name" + i)
                                .build()
                );
            }
        };

        // 조회 작업 (시간 측정)
        Runnable readTask = () -> {
            List<String> ulidIds = new ArrayList<>();
            userULIDRepository.findAll().forEach(user -> ulidIds.add(user.getId()));
            Collections.shuffle(ulidIds);

            StopWatch stopWatch = new StopWatch();
            stopWatch.start("ULID Read Performance");
            ulidIds.forEach(id -> userULIDRepository.findById(id));
            stopWatch.stop();

            System.out.println("ULID Concurrent Insert, Select: " + stopWatch.getTotalTimeSeconds() + " seconds");
            System.out.println(stopWatch.prettyPrint());
        };

        // 삽입 작업과 조회 작업 동시에 시작
        Future<?> insertFuture = executor.submit(insertTask);
        Future<?> readFuture = executor.submit(readTask);

        // 두 작업이 완료될 때까지 기다림
        insertFuture.get();
        readFuture.get();

        executor.shutdown();
    }
}
