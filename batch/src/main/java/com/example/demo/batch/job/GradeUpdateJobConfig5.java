package com.example.demo.batch.job;

import com.example.demo.entity.Grade;
import com.example.demo.entity.UserProjection;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class GradeUpdateJobConfig5 {

    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;
    private final JobExecutionListener listener;

    @Bean
    public Job gradeUpdateJob5() {
        return new JobBuilder("gradeUpdateJob5", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(gradeUpdateStep5())
                .build();
    }

    @Bean
    public Step gradeUpdateStep5() {
        return new StepBuilder("gradeUpdateStep5", jobRepository)
                .<UserProjection, UserProjection>chunk(1000, transactionManager)
                .reader(reader5())
                .writer(writer5())
                .build();
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<UserProjection> reader5() {
        return new JpaCursorItemReaderBuilder<UserProjection>()
                .name("userReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT u.id, u.totalSpent, u.grade FROM User u ORDER BY u.id") // Projection을 사용하기 위함
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<UserProjection> writer5() {
        return chunk -> {
            List<? extends UserProjection> users = chunk.getItems();

            // 새로운 등급으로 그룹화하여 ID 목록을 생성
            Map<Grade, List<Long>> gradeGroups = users.stream()
                    .map(user -> {
                        Grade newGrade = calculateGrade(user.getTotalSpent());
                        return new AbstractMap.SimpleEntry<>(user, newGrade);
                    })
                    .filter(entry -> !entry.getKey().getGrade().equals(entry.getValue()))
                    .collect(Collectors.groupingBy(
                            Map.Entry::getValue,
                            Collectors.mapping(entry -> entry.getKey().getId(), Collectors.toList())
                    ));

            // 각 등급 그룹에 대해 해당 그룹의 모든 사용자 ID의 등급을 업데이트합니다. (In Update & JDBC)
            gradeGroups.forEach((grade, ids) -> updateGradeInBatch(ids, grade));
        };
    }

    private void updateGradeInBatch(List<Long> ids, Grade grade) {
        String sql = "UPDATE users SET grade = ? WHERE id IN (?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // SQL 문에 대한 배치 작업 준비
            for (Long id : ids) {
                statement.setString(1, grade.name());
                statement.setLong(2, id);
                statement.addBatch();
            }

            // 배치 실행
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Grade calculateGrade(double totalSpent) {
        if (totalSpent > 8000) {
            return Grade.DIAMOND;
        } else if (totalSpent > 5000) {
            return Grade.PLATINUM;
        } else if (totalSpent > 3000) {
            return Grade.GOLD;
        } else if (totalSpent > 1000) {
            return Grade.SILVER;
        } else {
            return Grade.BRONZE;
        }
    }
}
