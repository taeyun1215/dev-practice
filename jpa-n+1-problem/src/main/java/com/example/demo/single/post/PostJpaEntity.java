package com.example.demo.single.post;

import com.example.demo.single.user.UserJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
public class PostJpaEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity userJpaEntity;

    public void addUser(UserJpaEntity userJpaEntity) {
        this.userJpaEntity = userJpaEntity;
    }
}