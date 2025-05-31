package com.example.demo.repository;

import com.example.demo.entity.Grade;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("UPDATE User u SET u.grade = :grade WHERE u.id IN :ids")
    void updateGrade(@Param("ids") List<Long> ids, @Param("grade") Grade grade);

}
