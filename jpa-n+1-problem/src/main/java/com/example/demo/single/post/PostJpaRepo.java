package com.example.demo.single.post;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepo extends JpaRepository<PostJpaEntity, Long> {

}