package com.example.demo.batch.job;

import com.example.demo.entity.Grade;
import com.example.demo.entity.UserProjection;
import com.example.demo.repository.UserRepository;
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

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class GradeUpdateJobConfig4 {

    private final UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final JobExecutionListener listener;

    @Bean
    public Job gradeUpdateJob4() {
        return new JobBuilder("gradeUpdateJob4", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(gradeUpdateStep4())
                .build();
    }

    @Bean
    public Step gradeUpdateStep4() {
        return new StepBuilder("gradeUpdateStep4", jobRepository)
                .<UserProjection, UserProjection>chunk(1000, transactionManager)
                .reader(reader4())
                .writer(writer4())
                .build();
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<UserProjection> reader4() {
        return new JpaCursorItemReaderBuilder<UserProjection>()
                .name("userReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT u.id, u.totalSpent, u.grade FROM User u ORDER BY u.id") // Projection을 사용하기 위함
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<UserProjection> writer4() {
        return chunk -> {
            // 새로운 등급으로 그룹화하여 ID 목록을 생성
            Map<Grade, List<Long>> gradeGroups = chunk.getItems().stream()
                    .map(user -> {
                        Grade newGrade = calculateGrade(user.getTotalSpent());
                        return new AbstractMap.SimpleEntry<>(user, newGrade);
                    })
                    .filter(entry -> !entry.getKey().getGrade().equals(entry.getValue()))
                    .collect(Collectors.groupingBy(
                            Map.Entry::getValue,
                            Collectors.mapping(entry -> entry.getKey().getId(), Collectors.toList())
                    ));

            // 각 등급 그룹에 대해 해당 그룹의 모든 사용자 ID의 등급을 업데이트합니다. (In Update & JPQL)
            gradeGroups.forEach((grade, ids) -> userRepository.updateGrade(ids, grade));
        };
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
