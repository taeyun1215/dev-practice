package com.example.demo.repository;

import com.example.demo.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends RepositoryTest {

    @Test
    void 유저_저장() {
        // given
        User user = new User("Alice", "alice@example.com");

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Alice");
    }

    @Test
    void 유저_조회() {
        // given
        User user = userRepository.save(new User("Bob", "bob@example.com"));

        // when
        User foundUser = userRepository.findById(user.getId()).orElseThrow();

        // then
        assertThat(foundUser.getName()).isEqualTo("Bob");
        assertThat(foundUser.getEmail()).isEqualTo("bob@example.com");
    }
}