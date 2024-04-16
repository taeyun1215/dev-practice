package com.example.demo;

import com.example.demo.basic.UserBasic;
import com.example.demo.basic.UserBasicRepository;
import com.example.demo.ulid.UserULID;
import com.example.demo.uuid.UserUUID;
import com.example.demo.uuid.UserUUIDRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class PerformanceTest {

    @Autowired
    private UserBasicRepository userBasicRepository;

    @Autowired
    private UserUUIDRepository userUUIDRepository;

    @Test
    public void testReadPerformance() {
        for (int i = 0; i < 100000; i++) {
            userBasicRepository.save(
                    UserBasic.builder()
                            .email("email" + i)
                            .name("name" + i)
                            .build()
            );
        }

        for (int i = 0; i < 100000; i++) {
            userUUIDRepository.save(
                    UserUUID.builder()
                            .email("email" + i)
                            .name("name" + i)
                            .build()
            );
        }

        StopWatch stopWatch = new StopWatch();

        List<Long> basicIds = new ArrayList<>();
        List<UUID> uuidIds = new ArrayList<>();

        userBasicRepository.findAll().forEach(user -> basicIds.add(user.getId()));
        userUUIDRepository.findAll().forEach(user -> uuidIds.add(user.getId()));

        stopWatch.start();
        for (Long id : basicIds) {
            userBasicRepository.findById(id);
        }
        stopWatch.stop();

        System.out.println("Query time with index: " + stopWatch.getTotalTimeSeconds() + " seconds");
        System.out.println(stopWatch.prettyPrint());

        stopWatch.start();

        for (UUID id : uuidIds) {
            userUUIDRepository.findById(id);
        }
        stopWatch.stop();

        System.out.println("Query time with index: " + stopWatch.getTotalTimeSeconds() + " seconds");
        System.out.println(stopWatch.prettyPrint());
    }
}
