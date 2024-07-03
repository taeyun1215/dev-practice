package com.example.demo.batch.job;

import com.example.demo.batch.listener.JobCompletionNotificationListener;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class GradeUpdateJobConfig {

    private final UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    @Bean
    public Job gradeUpdateJob() {
        return new JobBuilder("gradeUpdateJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .start(gradeUpdateStep())
                .build();
    }

    @Bean
    public Step gradeUpdateStep() {
        return new StepBuilder("registerUserAPI", jobRepository)
                .<User, User>chunk(1000, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<User> reader() {
        RepositoryItemReader<User> reader = new RepositoryItemReader<>();
        reader.setRepository(userRepository);
        reader.setMethodName("findAll");
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<User, User> processor() {
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
    public ItemWriter<User> writer() {
        return chunk -> {
            List<User> filteredItems = chunk.getItems().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (!filteredItems.isEmpty()) {
                userRepository.saveAll(filteredItems);
            }
        };
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener();
    }
}
