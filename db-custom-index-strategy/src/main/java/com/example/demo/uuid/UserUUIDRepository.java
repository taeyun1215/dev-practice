package com.example.demo.uuid;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserUUIDRepository extends JpaRepository<UserUUID, UUID> {
}
