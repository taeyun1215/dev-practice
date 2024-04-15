package com.example.demo;

import com.example.demo.custom.UserCustom;
import com.example.demo.custom.UserCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    private final UserCustomRepository userCustomRepository;

    public void createUser(String name, String email) {
        long userId = snowflakeIdGenerator.nextId();
        userCustomRepository.save(new UserCustom(userId, name, email));
    }
}
