package com.example.demo.single;

import com.example.demo.single.book.BookJpaEntity;
import com.example.demo.single.book.BookJpaRepo;
import com.example.demo.single.review.ReviewJpaEntity;
import com.example.demo.single.review.ReviewJpaRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
@SpringBootTest
@DisplayName("1:N 관계에서 N에서 EAGER Loading 을 사용할 때 발생하는 N+1 문제를 확인합니다.")
public class JPA_OneToMany_Eager_Loading_Test1 {

    @Autowired
    private BookJpaRepo bookJpaRepo;

    @Autowired
    private ReviewJpaRepo reviewJpaRepo;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void setup() {
        for (int i = 1; i <= 10; i++) {
            // BookJpaEntity 객체 생성
            BookJpaEntity bookJpaEntity = BookJpaEntity.builder()
                    .title("title" + i)
                    .author("author" + i)
                    .build();

            // ReviewJpaEntity 객체 생성
            ReviewJpaEntity reviewJpaEntity1 = ReviewJpaEntity.builder()
                    .content("content " + i)
                    .build();

            ReviewJpaEntity reviewJpaEntity2 = ReviewJpaEntity.builder()
                    .content("content " + i)
                    .build();

            // BookJpaEntity에 ReviewJpaEntity를 추가
            bookJpaEntity.addReview(reviewJpaEntity1);
            bookJpaEntity.addReview(reviewJpaEntity2);

            // ReviewJpaEntity에 BookJpaEntity를 추가
            reviewJpaEntity1.addBook(bookJpaEntity);
            reviewJpaEntity2.addBook(bookJpaEntity);

            // BookJpaEntity를 저장 (CascadeType.ALL에 의해 ReviewJpaEntity도 저장됩니다)
            bookJpaRepo.save(bookJpaEntity);
        }
    }

    @Test
    @DisplayName("EAGER Loading을 사용할 때 발생하는 N+1 문제를 확인합니다.")
    public void JPA_OneToMany_Eager_Loading_Test1() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        System.out.println("------------ BOOK 전체 조회 요청 ------------");
        List<BookJpaEntity> bookJpaEntities = bookJpaRepo.findAll();
        System.out.println("------------ BOOK 전체 조회 완료. [조회된 BOOK의 개수(N=10) 만큼 추가적인 쿼리 발생]------------\n");

        System.out.println("------------ BOOK와 연관관계인 REVIEW 내용 조회 요청------------");
        bookJpaEntities.forEach(bookJpaEntity -> System.out.println("BOOK IN REVIEW ID: " + bookJpaEntity.getReviewJpaEntities().get(0).getId()));
        System.out.println("------------ BOOK와 연관관계인 REVIEW 내용 조회 완료 [추가적인 쿼리 발생하지 않음] ------------\n");
    }

    @Test
    @DisplayName("Fetch Join을 사용하여 N+1 문제를 해결합니다.")
    public void JPA_OneToMany_Eager_Loading_Test2() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        System.out.println("------------ BOOK 전체 조회 요청 ------------");
        List<BookJpaEntity> bookJpaEntities = bookJpaRepo.findAllByFetchJoin();
        System.out.println("------------ BOOK 전체 조회 완료. [Fetch Join을 사용하여 1번의 쿼리만 발생함]------------\n");

        System.out.println("------------ BOOK와 연관관계인 REVIEW 내용 조회 요청------------");
        bookJpaEntities.forEach(bookJpaEntity -> System.out.println("BOOK IN REVIEW ID: " + bookJpaEntity.getReviewJpaEntities().get(0).getId()));
        System.out.println("------------ BOOK와 연관관계인 REVIEW 내용 조회 완료 [추가적인 쿼리 발생하지 않음] ------------\n");
    }
}