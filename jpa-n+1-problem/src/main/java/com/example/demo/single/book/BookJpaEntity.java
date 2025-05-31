package com.example.demo.single.book;

import com.example.demo.single.review.ReviewJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BookJpaEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String author;

    @Builder.Default
    @OneToMany(mappedBy = "bookJpaEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ReviewJpaEntity> reviewJpaEntities = new ArrayList<>();

    public void addReview(ReviewJpaEntity reviewJpaEntity) {
        reviewJpaEntities.add(reviewJpaEntity);
    }

}