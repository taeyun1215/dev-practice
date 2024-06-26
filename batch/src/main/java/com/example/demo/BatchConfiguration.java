package com.example.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final UserRepository userRepository;

    public BatchConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, Step step) {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("step")
                .tasklet((contribution, chunkContext) -> {
                    updateGrades();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private void updateGrades() {
        userRepository.findAll().forEach(user -> {
            if (user.getTotalSpent() > 5000) {
                user.setGrade(Grade.DIAMOND);
            } else if (user.getTotalSpent() > 3000) {
                user.setGrade(Grade.PLATINUM);
            } else if (user.getTotalSpent() > 1000) {
                user.setGrade(Grade.GOLD);
            } else if (user.getTotalSpent() > 500) {
                user.setGrade(Grade.SILVER);
            } else {
                user.setGrade(Grade.BRONZE);
            }
            userRepository.save(user);
        });
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener();
    }
}