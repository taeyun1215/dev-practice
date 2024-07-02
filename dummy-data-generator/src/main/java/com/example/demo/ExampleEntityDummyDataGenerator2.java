package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExampleEntityDummyDataGenerator2 {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/example-db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "taeyun1215";
    private static final int BATCH_SIZE = 10000;

    public static void main(String[] args) {
        try {
            // MariaDB JDBC 드라이버 로드
            Class.forName("org.mariadb.jdbc.Driver");

            // 데이터베이스 연결
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            conn.setAutoCommit(false);  // 트랜잭션 수동 관리

            // 더미 데이터 생성
            generateDummyExampleEntityData(conn);

            // 연결 종료
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateDummyExampleEntityData(Connection conn) throws SQLException {
        String insertQuery = "INSERT INTO example_entity (name, description) VALUES (?, ?)";

        // 데이터 생성을 위한 PreparedStatement 준비
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            // 더미 데이터 개수 (여기서는 100,000개로 가정)
            int numberOfDummyData = 100000;
            int count = 0;

            for (int i = 1; i <= numberOfDummyData; i++) {
                pstmt.setString(1, "name" + i); // name
                pstmt.setString(2, "description" + i); // description
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
}
