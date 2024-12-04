package com.techeer.abandoneddog.batch.config;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.animal.repository.PetInfoRepository;
import com.techeer.abandoneddog.batch.BatchListener;
import com.techeer.abandoneddog.batch.monitor.SlackNotifier;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.batch.core.Step;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final EntityManagerFactory emf;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PetInfoRepository petInfoRepository;
    private final BatchListener batchListener;
    private final SlackNotifier slackNotifier;


    @Bean
    public org.springframework.batch.core.Job deleteOldRecordsJob(Step deleteStep) {
        return new JobBuilder("deleteOldRecordsJob", jobRepository)
                .start(deleteStep)
                //.split(new SimpleAsyncTaskExecutor()).add(otherStep())
                .listener(batchListener) // 리스너 추가 (필요시)
                .build();
    }

    @Bean
    public Step deleteOldRecordsStep() {
        return new StepBuilder("deleteOldRecordsStep", jobRepository)
                .<Long, Long>chunk(500, transactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .listener(batchListener)
                .writer(repositoryItemWriter())
                .faultTolerant()  // 오류 발생 시 재시도 및 건너뛰기 허용
               .retry(Exception.class)  // Exception 발생 시 재시도
                .retryLimit(3)  // 최대 3회 재시도
                .skip(Exception.class)  // Exception 발생 시 건너뛰기
                .skipLimit(30)  // 최대 10번 건너뛰기
                .build();

    }

    @Bean
    public ItemReader<Long> itemReader() {
        String cutoffDate = LocalDate.now().minusWeeks(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        slackNotifier.notify("reader good");


        JpaPagingItemReader<Long> reader = new JpaPagingItemReaderBuilder<Long>()
                .name("jpaItemReader")
                .entityManagerFactory(emf)
                .queryString("SELECT p.id FROM PetInfo p WHERE p.happenDt < :cutoffDate")
                .parameterValues(Map.of("cutoffDate", cutoffDate))
                .pageSize(1000)
                .build();

        try {
            slackNotifier.notify("trymoon initializing reader: ");
            // StepExecution에서 ExecutionContext를 가져와서 open() 호출
//            TaskletStep taskletStep = (TaskletStep) jobExecution.getExecutionContext().get("taskletStep");
//            ExecutionContext executionContext = taskletStep.getExecutionContext();
//            reader.open(executionContext);  // ExecutionContext 전달

        } catch (Exception e) {
            slackNotifier.notify("Error initializing reader: " + e.getMessage());
            throw e;
        }

        return reader;
    }

//        return new JpaPagingItemReaderBuilder<Long>()
//                .name("jpaItemReader")
//                .entityManagerFactory(emf)
//               .queryString("SELECT p.id FROM PetInfo p WHERE p.happenDt < :cutoffDate")
//               // .queryString("SELECT p FROM PetInfo p WHERE p.happenDt < :cutoffDate")
//                .parameterValues(Map.of("cutoffDate", cutoffDate))
//                .pageSize(1000)
//                .build();



    @Bean
    public ItemProcessor<Long, Long> itemProcessor() {
        return item -> {

            //System.out.println("Processing item: " + item);
            return item;
        };
    }


    @Bean
    public ItemWriter<Long> repositoryItemWriter() {
        slackNotifier.notify("writef good");
        return new ItemWriter<Long>() {
int i=0;

            @Override
            public void write(Chunk<? extends Long> items) throws Exception {
                for (Long item : items) {
        i++;
                    //System.out.println("Deleting item: " + item);
                    petInfoRepository.deleteById(item);  // Repository를 사용하여 삭제
                }
                slackNotifier.notify(String.valueOf(i));
            }
        };
    }

}
