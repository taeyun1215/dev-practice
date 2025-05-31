package com.example.demo.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public abstract class RepositoryTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PostRepository postRepository;
}