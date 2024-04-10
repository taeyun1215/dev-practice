package com.example.demo.category;

public interface CategoryService {

    void categoryRegister(CategoryRegisterRequest categoryRegisterRequest);

    Category getCategory(Long id);

}
