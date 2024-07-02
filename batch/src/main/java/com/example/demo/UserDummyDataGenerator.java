package com.example.demo;

import com.example.demo.entity.Grade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class UserDummyDataGenerator {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/batch-test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "6548";
    private static final Random RANDOM = new Random();
    private static final int BATCH_SIZE = 5000;

    public static void main(String[] args) {
        try {
            // MariaDB JDBC 드라이버 로드
            Class.forName("org.mariadb.jdbc.Driver");

            // 데이터베이스 연결
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            conn.setAutoCommit(false);  // 트랜잭션 수동 관리

            // 더미 데이터 생성
            generateDummyUserData(conn);

            // 연결 종료
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateDummyUserData(Connection conn) throws SQLException {
        String insertQuery = "INSERT INTO users (name, total_spent, grade) VALUES (?, ?, ?)";

        // 데이터 생성을 위한 PreparedStatement 준비
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            int numberOfDummyData = 50000;
            int count = 0;

            for (int i = 1; i <= numberOfDummyData; i++) {
                pstmt.setString(1, "name" + i);
                double totalSpent = RANDOM.nextDouble() * 10000;
                pstmt.setDouble(2, totalSpent); // totalSpent
                pstmt.setString(3, getGrade(totalSpent).name()); // grade
                pstmt.addBatch();

                if (++count % BATCH_SIZE == 0) {
                    pstmt.executeBatch();  // 배치 실행
                    conn.commit();  // 트랜잭션 커밋
                }
            }

            pstmt.executeBatch();  // 남아있는 배치 실행
            conn.commit();  // 최종 커밋
        }
    }

    private static Grade getGrade(double totalSpent) {
        if (totalSpent > 5000) {
            return Grade.DIAMOND;
        } else if (totalSpent > 3000) {
            return Grade.PLATINUM;
        } else if (totalSpent > 1000) {
            return Grade.GOLD;
        } else if (totalSpent > 500) {
            return Grade.SILVER;
        } else {
            return Grade.BRONZE;
        }
    }
}