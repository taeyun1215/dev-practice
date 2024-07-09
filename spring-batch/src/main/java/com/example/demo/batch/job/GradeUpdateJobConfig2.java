package com.example.demo.batch.job;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.Grade;
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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class GradeUpdateJobConfig2 {

    private final UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final JobExecutionListener listener;

    @Bean
    public Job gradeUpdateJob2() {
        return new JobBuilder("gradeUpdateJob2", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(gradeUpdateStep2())
                .build();
    }

    @Bean
    public Step gradeUpdateStep2() {
        return new StepBuilder("gradeUpdateStep2", jobRepository)
                .<User, User>chunk(1000, transactionManager)
                .reader(reader2())
                .processor(processor2())
                .writer(writer2())
                .build();
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<User> reader2() {
        return new JpaCursorItemReaderBuilder<User>()
                .name("userReader2")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT u FROM User u ORDER BY u.id")
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<User, User> processor2() {
        return user -> {
            Grade originalGrade = user.getGrade();
            Grade newGrade = calculateGrade(user.getTotalSpent());
            if (!newGrade.equals(originalGrade)) {
                return user.toBuilder().grade(newGrade).build();  // 변경된 경우 새로운 User 객체 반환
            }
            return null;  // 변경되지 않은 경우 null 반환
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

    @Bean
    @StepScope
    public ItemWriter<User> writer2() {
        return chunk -> {
            List<User> filteredItems = chunk.getItems().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (!filteredItems.isEmpty()) {
                userRepository.saveAll(filteredItems);
            }
        };
    }
}
