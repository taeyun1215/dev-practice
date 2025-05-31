package com.example.demo.snowflake;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserSnowflakeRepository extends JpaRepository<UserSnowflake, UUID> {
}
