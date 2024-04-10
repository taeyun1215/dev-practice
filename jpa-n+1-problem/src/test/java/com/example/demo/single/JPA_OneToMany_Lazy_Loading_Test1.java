package com.example.demo.single;

import com.example.demo.single.post.PostJpaEntity;
import com.example.demo.single.post.PostJpaRepo;
import com.example.demo.single.user.UserJpaEntity;
import com.example.demo.single.user.UserJpaRepo;
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
@DisplayName("1:N 관계에서 N에서 LAZY Loading 을 사용할 때 발생하는 N+1 문제를 확인합니다.")
public class JPA_OneToMany_Lazy_Loading_Test1 {

    @Autowired
    private UserJpaRepo userJpaRepo;

    @Autowired
    private PostJpaRepo postJpaRepo;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void setup() {
        for (int i = 1; i <= 10; i++) {
            // UserJpaEntity 객체 생성
            UserJpaEntity userJpaEntity = UserJpaEntity.builder()
                    .email("user" + i + "@example.com")
                    .password("password")
                    .nickname("nickname" + i)
                    .name("User " + i)
                    .build();

            // PostJpaEntity 객체 생성
            PostJpaEntity postJpaEntity1 = PostJpaEntity.builder()
                    .title("Title " + i)
                    .content("Content " + i)
                    .build();

            PostJpaEntity postJpaEntity2 = PostJpaEntity.builder()
                    .title("Title " + i)
                    .content("Content " + i)
                    .build();

            // UserJpaEntity에 PostJpaEntity를 추가
            userJpaEntity.addPost(postJpaEntity1);
            userJpaEntity.addPost(postJpaEntity2);

            // PostJpaEntity에 UserJpaEntity를 추가
            postJpaEntity1.addUser(userJpaEntity);
            postJpaEntity2.addUser(userJpaEntity);

            // UserJpaEntity를 저장 (CascadeType.ALL에 의해 PostJpaEntity도 저장됩니다)
            userJpaRepo.save(userJpaEntity);
        }
    }

    @Test
    @DisplayName("Lazy Loading을 사용할 때 발생하는 N+1 문제를 확인합니다.")
    public void test1() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        System.out.println("------------ USER 전체 조회 요청 ------------");
        List<UserJpaEntity> userJpaEntities = userJpaRepo.findAll();
        System.out.println("------------ USER 전체 조회 완료. [1번의 쿼리 발생]------------\n");

        System.out.println("------------ USER와 연관관계인 POST 내용 조회 요청 ------------");
        userJpaEntities.forEach(userJpaEntity -> System.out.println("USER IN POST ID: " + userJpaEntity.getPostJpaEntities().get(0).getId()));
        System.out.println("------------ USER와 연관관계인 POST 내용 조회 완료  [조회된 USER 개수(N=10) 만큼 추가적인 쿼리 발생] ------------\n");
    }

    @Test
    @DisplayName("Fetch Join을 사용하여 N+1 문제를 해결합니다.")
    public void test2() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        System.out.println("------------ USER 전체 조회 요청 ------------");
        List<UserJpaEntity> userJpaEntities = userJpaRepo.findAllByFetchJoin();
        System.out.println("------------ USER 전체 조회 완료. [1번의 쿼리 발생]------------\n");

        System.out.println("------------ USER와 연관관계인 POST 내용 조회 요청 ------------");
        userJpaEntities.forEach(userJpaEntity -> System.out.println("USER IN POST ID: " + userJpaEntity.getPostJpaEntities().get(0).getId()));
        System.out.println("------------ USER와 연관관계인 POST 내용 조회 완료  [Fetch Join을 사용하여 추가적인 쿼리 발생하지 않음] ------------\n");

        System.out.println("------------ USER SIZE 확인 ------------");
        System.out.println("userJpaEntities.size() : " + userJpaEntities.size());
        System.out.println("------------ USER SIZE 확인 완료 [20개가 조회!?, Fetch Join(Inner Join)은 카테시안 곱이 발생하여 POST의 수만큼 USER가 중복이 발생]------------\n");

        for (UserJpaEntity userJpaEntity : userJpaEntities) {
            System.out.println(userJpaEntity.getId());
        }
    }

    @Test
    @DisplayName("Fetch Join을 사용하여 N+1 문제를 해결합니다. (Distinct)")
    public void test3() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        System.out.println("------------ USER 전체 조회 요청 ------------");
        List<UserJpaEntity> userJpaEntities = userJpaRepo.findAllByFetchJoinDistinct();
        System.out.println("------------ USER 전체 조회 완료. [1번의 쿼리 발생]------------\n");

        System.out.println("------------ USER와 연관관계인 POST 내용 조회 요청 ------------");
        userJpaEntities.forEach(userJpaEntity -> System.out.println("USER IN POST ID: " + userJpaEntity.getPostJpaEntities().get(0).getId()));
        System.out.println("------------ USER와 연관관계인 POST 내용 조회 완료  [Fetch Join을 사용하여 추가적인 쿼리 발생하지 않음] ------------\n");

        System.out.println("------------ USER SIZE 확인 ------------");
        System.out.println("userJpaEntities.size() : " + userJpaEntities.size());
        System.out.println("------------ USER SIZE 확인 완료 [10개가 조회 된다]------------\n");
    }

    @Test
    @DisplayName("EntityGraph를 사용하여 N+1 문제를 해결합니다.")
    public void test4() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        System.out.println("------------ USER 전체 조회 요청 ------------");
        List<UserJpaEntity> userJpaEntities = userJpaRepo.findAllByEntityGraph();
        System.out.println("------------ USER 전체 조회 완료. [1번의 쿼리 발생]------------\n");

        System.out.println("------------ USER와 연관관계인 POST 내용 조회 요청 ------------");
        userJpaEntities.forEach(userJpaEntity -> System.out.println("USER IN POST ID: " + userJpaEntity.getPostJpaEntities().get(0).getId()));
        System.out.println("------------ USER와 연관관계인 POST 내용 조회 완료  [Fetch Join을 사용하여 추가적인 쿼리 발생하지 않음] ------------\n");

        System.out.println("------------ USER SIZE 확인 ------------");
        System.out.println("userJpaEntities.size() : " + userJpaEntities.size());
        System.out.println("------------ USER SIZE 확인 완료 [20개가 조회!?, EntityGraph(Outer Join)도 카테시안 곱이 발생하여 POST의 수만큼 USER가 중복이 발생]------------\n");
    }
}