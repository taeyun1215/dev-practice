package com.example.demo.single.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserJpaRepo extends JpaRepository<UserJpaEntity, Long> {

    @Query("select u from UserJpaEntity u join fetch u.postJpaEntities")
    List<UserJpaEntity> findAllByFetchJoin();

    @Query("select DISTINCT u from UserJpaEntity u join fetch u.postJpaEntities")
    List<UserJpaEntity> findAllByFetchJoinDistinct();

    @EntityGraph(attributePaths = {"postJpaEntities"})
    @Query("select u from UserJpaEntity u")
    List<UserJpaEntity> findAllByEntityGraph();

}
