package com.example.demo.batch.job;

import com.example.demo.batch.listener.JobCompletionNotificationListener;
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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class GradeUpdateJobConfig3 {

    private final UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final JobExecutionListener listener;

    @Bean
    public Job gradeUpdateJob3() {
        return new JobBuilder("gradeUpdateJob3", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(gradeUpdateStep3())
                .build();
    }

    @Bean
    public Step gradeUpdateStep3() {
        return new StepBuilder("gradeUpdateStep3", jobRepository)
                .<User, User>chunk(1000, transactionManager)
                .reader(reader3())
                .writer(writer3())
                .build();
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<User> reader3() {
        return new JpaCursorItemReaderBuilder<User>()
                .name("userReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT u FROM User u ORDER BY u.id")
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<User> writer3() {
        return chunk -> {
            List<User> updatedUsers = chunk.getItems().stream()
                    .map(user -> {
                        Grade newGrade = calculateGrade(user.getTotalSpent());
                        if (!newGrade.equals(user.getGrade())) {
                            return user.toBuilder().grade(newGrade).build();  // 변경된 경우 새로운 User 객체 반환
                        }
                        return user;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (!updatedUsers.isEmpty()) {
                userRepository.saveAll(updatedUsers);
            }
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
