package com.example.demo.batch.job;

import com.example.demo.batch.listener.JobCompletionNotificationListener;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

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
        return new StepBuilder("gradeUpdateStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    updateGrades();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
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