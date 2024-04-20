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

@SpringBootTest
public class RandomSelectPerformanceTest {

    @Autowired
    private UserBasicRepository userBasicRepository;

    @Autowired
    private UserUUIDRepository userUUIDRepository;

    @Autowired
    private UserULIDRepository userULIDRepository;

    @Test
    public void testReadPerformanceBasic() {
        List<UserBasic> usersBasic = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            usersBasic.add(UserBasic.builder().email("email" + i).name("name" + i).build());
        }
        userBasicRepository.saveAll(usersBasic);

        List<Long> basicIds = new ArrayList<>();
        userBasicRepository.findAll().forEach(user -> basicIds.add(user.getId()));

        // 무작위 정렬
        Collections.shuffle(basicIds);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (Long id : basicIds) {
            userBasicRepository.findById(id);
        }
        stopWatch.stop();

        System.out.println("Random Auto Select: " + stopWatch.getTotalTimeSeconds() + " seconds");
        System.out.println(stopWatch.prettyPrint());
    }

    @Test
    public void testReadPerformanceUUID() {
        List<UserUUID> usersUUID = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            usersUUID.add(UserUUID.builder().email("email" + i).name("name" + i).build());
        }
        userUUIDRepository.saveAll(usersUUID);

        List<UUID> uuidIds = new ArrayList<>();
        userUUIDRepository.findAll().forEach(user -> uuidIds.add(user.getId()));

        // 무작위 정렬
        Collections.shuffle(uuidIds);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (UUID id : uuidIds) {
            userUUIDRepository.findById(id);
        }
        stopWatch.stop();

        System.out.println("Random UUID Select: " + stopWatch.getTotalTimeSeconds() + " seconds");
        System.out.println(stopWatch.prettyPrint());
    }

    @Test
    public void testReadPerformanceULID() {
        List<UserULID> usersULID = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            usersULID.add(UserULID.builder().email("email" + i).name("name" + i).build());
        }
        userULIDRepository.saveAll(usersULID);

        List<String> ulidIds = new ArrayList<>();
        userULIDRepository.findAll().forEach(user -> ulidIds.add(user.getId()));

        // 무작위 정렬
        Collections.shuffle(ulidIds);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (String id : ulidIds) {
            userULIDRepository.findById(id);
        }
        stopWatch.stop();

        System.out.println("Random ULID Select : " + stopWatch.getTotalTimeSeconds() + " seconds");
        System.out.println(stopWatch.prettyPrint());
    }
}
