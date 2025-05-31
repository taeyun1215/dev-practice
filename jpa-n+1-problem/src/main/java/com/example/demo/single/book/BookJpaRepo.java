package com.example.demo.single.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookJpaRepo extends JpaRepository<BookJpaEntity, Long> {

    @Query("select b from BookJpaEntity b join fetch b.reviewJpaEntities")
    List<BookJpaEntity> findAllByFetchJoin();

}