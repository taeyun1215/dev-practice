package com.example.demo.ulid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Getter
@Builder
@Table(name = "user_ulid")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserULID {

    @Id
    @GeneratedValue(generator = "ulid-generator")
    @GenericGenerator(name = "ulid-generator", strategy = "com.example.demo.ulid.ULIDGenerator")
    private String id;

    private String name;
    private String email;

}