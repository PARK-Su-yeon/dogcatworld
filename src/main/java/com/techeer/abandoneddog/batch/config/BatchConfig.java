package com.techeer.abandoneddog.batch.config;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.animal.repository.PetInfoRepository;
import com.techeer.abandoneddog.batch.BatchListener;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
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


    @Bean
    public org.springframework.batch.core.Job deleteOldRecordsJob(Step deleteStep) {
        return new JobBuilder("deleteOldRecordsJob", jobRepository)
                .start(deleteStep)
                .listener(batchListener) // 리스너 추가 (필요시)
                .build();
    }

    @Bean
    public Step deleteOldRecordsStep() {
        return new StepBuilder("deleteOldRecordsStep", jobRepository)
                .<PetInfo, PetInfo>chunk(1000, transactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(repositoryItemWriter())
                .faultTolerant()  // 오류 발생 시 재시도 및 건너뛰기 허용
                .retry(Exception.class)  // Exception 발생 시 재시도
                .retryLimit(3)  // 최대 3회 재시도
                .skip(Exception.class)  // Exception 발생 시 건너뛰기
                .skipLimit(10)  // 최대 10번 건너뛰기
                .build();
    }

    @Bean
    public ItemReader<PetInfo> itemReader() {
        String cutoffDate = LocalDate.now().minusWeeks(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return new JpaPagingItemReaderBuilder<PetInfo>()
                .name("jpaItemReader")
                .entityManagerFactory(emf)
                .queryString("SELECT p FROM PetInfo p WHERE p.happenDt < :cutoffDate")
                .parameterValues(Map.of("cutoffDate", cutoffDate))
                .pageSize(1000)
                .build();
    }


    @Bean
    public ItemProcessor<PetInfo, PetInfo> itemProcessor() {
        return item -> {

            System.out.println("Processing item: " + item);
            return item;
        };
    }


    @Bean
    public ItemWriter<PetInfo> repositoryItemWriter() {
        return new ItemWriter<PetInfo>() {


            @Override
            public void write(Chunk<? extends PetInfo> items) throws Exception {
                for (PetInfo item : items) {
                    System.out.println("Deleting item: " + item);
                    petInfoRepository.delete(item);  // Repository를 사용하여 삭제
                    System.out.println("after Deleting item: " + item.getId());
                }
            }
        };
    }

}
