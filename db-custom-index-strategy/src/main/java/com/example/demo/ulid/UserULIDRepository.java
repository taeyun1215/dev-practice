package com.example.demo.ulid;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserULIDRepository extends JpaRepository<UserULID, String> {
}
