package com.example.demo.util;

import java.sql.*;
import java.util.Random;

public class ProductDummyDataGenerator {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/JPA_N+1_ISSUE";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "6548";
    private static final String[] PRODUCT_NAMES = {"Product A", "Product B", "Product C"};

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Class.forName("org.mariadb.jdbc.Driver");

            generateDummyProductData(conn);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateDummyProductData(Connection conn) throws SQLException {
        final String insertCategoryQuery = "INSERT INTO categorys (name, description1, description2, description3) VALUES (?, ?, ?, ?)";
        final String insertProductQuery = "INSERT INTO products (name, price, category_id) VALUES (?, ?, ?)";
        Random random = new Random();

        for (int categoryId = 1; categoryId <= 100; categoryId++) {
            int createdCategoryId = insertCategory(conn, insertCategoryQuery, "Electronics " + categoryId, random);
            insertProductsForCategory(conn, insertProductQuery, createdCategoryId, random);
        }
    }

    private static int insertCategory(Connection conn, String query, String categoryName, Random random) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, categoryName);
            pstmt.setString(2, "Description 1 for " + categoryName);
            pstmt.setString(3, "Description 2 for " + categoryName);
            pstmt.setString(4, "Description 3 for " + categoryName);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    private static void insertProductsForCategory(Connection conn, String query, int categoryId, Random random) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            int numberOfProducts = 5 + random.nextInt(16); // 5 ~ 20개

            for (int i = 1; i <= numberOfProducts; i++) {
                pstmt.setString(1, PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)] + " " + i);
                pstmt.setDouble(2, random.nextDouble() * 100);
                pstmt.setInt(3, categoryId);
                pstmt.executeUpdate();
            }
        }
    }
}