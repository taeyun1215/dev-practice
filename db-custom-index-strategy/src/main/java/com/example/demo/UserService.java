package com.example.demo;

import com.example.demo.snowflake.SnowflakeIdGenerator;
import com.example.demo.snowflake.UserSnowflake;
import com.example.demo.snowflake.UserSnowflakeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    private final UserSnowflakeRepository userCustomRepository;

    public void createUser(String name, String email) {
        long userId = snowflakeIdGenerator.nextId();
        userCustomRepository.save(new UserSnowflake(userId, name, email));
    }
}
