package com.example.demo.custom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserCustomRepository extends JpaRepository<UserCustom, UUID> {
}
