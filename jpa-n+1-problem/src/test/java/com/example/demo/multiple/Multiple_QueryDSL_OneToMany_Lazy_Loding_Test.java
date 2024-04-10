package com.example.demo.multiple;

import com.example.demo.multiple.category.CategoryRepo;
import com.example.demo.multiple.category.QCategory;
import com.example.demo.multiple.customer.CustomerRepo;
import com.example.demo.multiple.customer.QCustomer;
import com.example.demo.multiple.product.Product;
import com.example.demo.multiple.product.ProductDetails1;
import com.example.demo.multiple.product.ProductDetails2;
import com.example.demo.multiple.product.ProductRepo;
import com.example.demo.multiple.review.QReview;
import com.example.demo.multiple.review.Review;
import com.example.demo.multiple.review.ReviewRepo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Stream;

@Transactional
@SpringBootTest
public class Multiple_QueryDSL_OneToMany_Lazy_Loding_Test {

    @Autowired
    private EntityManager em;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private CustomerRepo customerRepo;

    QProduct product = QProduct.product;
    QCategory category = QCategory.category;
    QReview review = QReview.review;
    QCustomer customer = QCustomer.customer;

    @BeforeEach
    void setup() {
//        for (int i = 0; i < 2; i++) {
//            Category category = Category.builder()
//                    .name("category_name" + i)
//                    .build();
//            categoryRepo.save(category);
//
//            for (int j = 0; j < 2; j++) {
//                Product product = Product.builder()
//                        .name("product_name" + j)
//                        .price(10.0)
//                        .category(category)
//                        .build();
//                productRepo.save(product);
//
//                for (int k = 0; k < 2; k++) {
//                    Customer customer = Customer.builder()
//                            .name("customer_name" + k)
//                            .email("customer_email" + k)
//                            .build();
//                    customerRepo.save(customer);
//
//                    for (int l = 0; l < 2; l++) {
//                        Review review = Review.builder()
//                                .content("review_content" + l)
//                                .rating(5)
//                                .product(product)
//                                .customer(customer)
//                                .build();
//                        reviewRepo.save(review);
//                    }
//                }
//            }
//        }
    }

    @Test
    @DisplayName("QueryDSL을 사용할 때 발생하는 N+1 문제를 확인합니다.")
    public void test1() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<Product> products = queryFactory
                .selectFrom(product)
                .fetch();

        for (Product p : products) {
            System.out.println("Product: " + p.getName());
            System.out.println("Category: " + p.getCategory().getName());
            for (Review r : p.getReviews()) {
                System.out.println("Review: " + r.getContent());
                System.out.println("Customer: " + r.getCustomer().getName());
            }
        }
    }

    @Test
    @DisplayName("QueryDSL과 Fetch Join을 사용하여 N+1 문제 해결")
    public void test2() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<Product> products = queryFactory
                .selectFrom(product)
                .leftJoin(product.reviews, review).fetchJoin()
                .leftJoin(product.category, category).fetchJoin()
                .fetch();

        for (Product p : products) {
            System.out.println("Product: " + p.getName());
            System.out.println("Category: " + p.getCategory().getName());
            for (Review r : p.getReviews()) {
                System.out.println("Review: " + r.getContent());
                System.out.println("Customer: " + r.getCustomer().getName());
            }
        }
    }

    @Test
    @DisplayName("Product 안에 category 데이터 다 가져오기")
    public void test3() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        queryFactory
                .select(Projections.fields(ProductDetails1.class,
                        product.id.as("productId"),
                        product.name.as("productName"),
                        product.price.as("productPrice"),
                        product.category.as("category")))
                .from(product)
                .where(product.price.gt(0))
                .orderBy(product.name.asc())
                .fetch();
    }

    @Test
    @DisplayName("Product 안에 category Id 만 가져오기")
    public void test4() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QProduct product = QProduct.product;

        List<ProductDetails2> results = queryFactory
                .select(Projections.fields(ProductDetails2.class,
                        product.id.as("productId"),
                        product.name.as("productName"),
                        product.price.as("productPrice"),
                        product.category.id.as("categoryId")))
                .from(product)
                .where(product.price.gt(0))
                .orderBy(product.name.asc())
                .limit(1000)
                .fetch();

        for (ProductDetails2 detail : results) {
            System.out.println("Product: " + detail.getProductName());
        }
    }

    @Test
    @DisplayName("Product 안에 category Id 만 가져오기 with Pagination")
    public void test5() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QProduct product = QProduct.product;

        int pageNumber = 100000; // 페이지 번호 (예: 1)
        int pageSize = 20; // 페이지 당 항목 수 (예: 10)
        long offset = (pageNumber - 1) * pageSize; // 페이지 시작 위치 계산

        List<ProductDetails2> results = queryFactory
                .select(Projections.fields(ProductDetails2.class,
                        product.id.as("productId"),
                        product.name.as("productName"),
                        product.price.as("productPrice"),
                        product.category.id.as("categoryId")))
                .from(product)
                .where(product.price.gt(0))
                .orderBy(product.name.asc())
                .offset(offset) // 페이지 시작 위치 설정
                .limit(pageSize) // 페이지 당 항목 수 설정
                .fetch();
    }

    @Test
    @DisplayName("Product 안에 category Id 만 가져오기 with Streaming")
    public void test6() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QProduct product = QProduct.product;

        try (Stream<ProductDetails2> stream = queryFactory
                .select(Projections.fields(ProductDetails2.class,
                        product.id.as("productId"),
                        product.name.as("productName"),
                        product.price.as("productPrice"),
                        product.category.id.as("categoryId")))
                .from(product)
                .where(product.price.gt(0))
                .orderBy(product.name.asc())
                .stream()) {

            stream.forEach(productDetails2 -> {
                // 각 Product 객체에 대한 처리
                System.out.println(productDetails2.getProductName());
            });
        }
    }

    @Test
    @DisplayName("Product 안에 category Id 만 가져오기 with Pagination (No Offset)")
    public void test7() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        int pageSize = 20; // 페이지 당 항목 수 (예: 20)
        Long productId = 100000L; // 이전 페이지의 마지막 ID

        List<ProductDetails2> results = queryFactory
                .select(Projections.fields(ProductDetails2.class,
                        product.id.as("productId"),
                        product.name.as(    "productName"),
                        product.price.as("productPrice"),
                        product.category.id.as("categoryId")))
                .from(product)
                .where(product.price.gt(0), ltProductId(productId)) // No Offset 조건 추가
                .orderBy(product.id.desc()) // ID 기준 정렬
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression ltProductId(Long productId) {
        if (productId == null) {
            return null; // BooleanExpression 자리에 null이 반환되면 조건문에서 자동으로 제거된다.
        }

        return product.id.lt(productId);
    }

    @Test
    @DisplayName("Product 안에 category Id 만 가져오기 with Covering Index")
    public void test8() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QProduct product = QProduct.product;

        int pageNumber = 1000000; // 페이지 번호
        int pageSize = 20; // 페이지 당 항목 수
        long offset = (pageNumber - 1) * pageSize; // 페이지 시작 위치 계산

        // 첫 번째 쿼리: ID만 조회
        List<Long> productIds = queryFactory
                .select(product.id)
                .from(product)
                .where(product.price.gt(0))
                .orderBy(product.id.asc())
                .offset(offset)
                .limit(pageSize)
                .fetch();

        // 두 번째 쿼리: 실제 데이터 조회
        queryFactory
                .select(Projections.fields(ProductDetails2.class,
                        product.id.as("productId"),
                        product.name.as("productName"),
                        product.price.as("productPrice"),
                        product.category.id.as("categoryId")))
                .from(product)
                .where(product.id.in(productIds))
                .fetch();
    }

    @Test
    @DisplayName("Product 안에 category Id 만 가져오기 with Pagination (No Offset)")
    public void test9() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        int pageSize = 20; // 페이지 당 항목 수 (예: 20)
        Long productId = 100000L; // 이전 페이지의 마지막 ID

        List<ProductDetails2> results = queryFactory
                .select(Projections.fields(ProductDetails2.class,
                        product.id.as("productId"),
                        product.name.as(    "productName"),
                        product.price.as("productPrice"),
                        product.category.id.as("categoryId")))
                .from(product)
                .where(product.price.gt(0), ltProductId(productId)) // No Offset 조건 추가
                .orderBy(product.id.desc()) // ID 기준 정렬
                .limit(pageSize)
                .fetch();
    }
}
