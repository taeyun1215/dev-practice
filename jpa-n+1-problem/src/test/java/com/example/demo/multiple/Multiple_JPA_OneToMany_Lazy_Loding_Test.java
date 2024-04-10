package com.example.demo.multiple;

import com.example.demo.multiple.category.Category;
import com.example.demo.multiple.category.CategoryRepo;
import com.example.demo.multiple.customer.Customer;
import com.example.demo.multiple.customer.CustomerRepo;
import com.example.demo.multiple.product.Product;
import com.example.demo.multiple.product.ProductRepo;
import com.example.demo.multiple.review.Review;
import com.example.demo.multiple.review.ReviewRepo;
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
public class Multiple_JPA_OneToMany_Lazy_Loding_Test {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setup() {
        for (int i = 0; i < 2; i++) {
            Category category = Category.builder()
                    .name("category_name" + i)
                    .build();
            categoryRepo.save(category);

            for (int j = 0; j < 2; j++) {
                Product product = Product.builder()
                        .name("product_name" + j)
                        .price(10.0)
                        .category(category)
                        .build();
                productRepo.save(product);

                for (int k = 0; k < 2; k++) {
                    Customer customer = Customer.builder()
                            .name("customer_name" + k)
                            .email("customer_email" + k)
                            .build();
                    customerRepo.save(customer);

                    for (int l = 0; l < 2; l++) {
                        Review review = Review.builder()
                                .content("review_content" + l)
                                .rating(5)
                                .product(product)
                                .customer(customer)
                                .build();
                        reviewRepo.save(review);
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Lazy Loading을 사용할 때 발생하는 N+1 문제를 확인합니다.")
    void Multiple_JPA_OneToMany_Lazy_Loading_Test1() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        System.out.println("--------------PRODUCT 조회--------------");
        List<Product> products = productRepo.findAll();
        System.out.println("--------------PRODUCT 조회 끝---------------\n");

        for (Product product : products) {
            System.out.println("PRODUCT : " + product.getName());
            System.out.println("CATEGORY : " + product.getCategory().getName());
            for (Review review : product.getReviews()) {
                System.out.println("REVIEW : " + review.getContent());
                System.out.println("CUSTOMER : " + review.getCustomer().getName());
            }
        }
    }

    @Test
    @DisplayName("Lazy Loading을 사용할 때 발생하는 N+1 문제를 해결합니다.")
    void Multiple_JPA_OneToMany_Lazy_Loading_Test2() {
        em.flush();
        em.clear();
        System.out.println("------------ 영속성 컨텍스트 비우기 -----------\n");

        System.out.println("--------------PRODUCT 조회--------------");
        List<Product> products = productRepo.findAllByMultiFetchJoin();
        System.out.println("--------------PRODUCT 조회 끝---------------\n");

        for (Product product : products) {
            System.out.println("PRODUCT : " + product.getName());
            System.out.println("CATEGORY : " + product.getCategory().getName());
            for (Review review : product.getReviews()) {
                System.out.println("REVIEW : " + review.getContent());
                System.out.println("CUSTOMER : " + review.getCustomer().getName());
            }
        }
    }
}
