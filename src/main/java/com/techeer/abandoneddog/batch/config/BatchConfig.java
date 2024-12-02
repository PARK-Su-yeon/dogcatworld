package com.techeer.abandoneddog.batch.config;

import com.querydsl.jpa.impl.JPAQuery;
import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.animal.entity.QPetInfo;
import com.techeer.abandoneddog.animal.repository.PetInfoRepository;
import com.techeer.abandoneddog.batch.BatchListener;
import com.techeer.abandoneddog.batch.querydsl.CustomJpaPagingItemReader;
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
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Step;
//
//@Configuration
//@EnableBatchProcessing
//@RequiredArgsConstructor
//public class BatchConfig {
//
//    private final EntityManagerFactory emf;
//    @PersistenceContext
//    private final EntityManager entityManager;
//    private final JobRepository jobRepository;
//    private final PlatformTransactionManager transactionManager;
//    private final EntityManagerFactory entityManagerFactory;
//
//    @Bean
//    public org.springframework.batch.core.Job deleteOldRecordsJob(Step deleteStep) {
//        return new JobBuilder("deleteOldRecordsJob", jobRepository)
//                .start(deleteStep)
//                .listener(new BatchListener())
//                .build();
//    }
//
//
//
//
//    @Bean
//    public Step deleteOldRecordsStep() {
//        return new StepBuilder("deleteOldRecordsStep", jobRepository)
//                .<PetInfo, PetInfo>chunk(1000, transactionManager)
//                .reader(itemReader())
//                .processor(itemProcessor())
//                .writer(itemWriter())
//                .faultTolerant()  // 오류 발생 시 재시도 및 건너뛰기 허용
//                .retry(Exception.class)  // Exception 발생 시 재시도
//                .retryLimit(3)  // 최대 3회 재시도
//                .skip(Exception.class)  // Exception 발생 시 건너뛰기
//                .skipLimit(10)  // 최대 10번 건너뛰기
//                .build();
//    }
//
//    @Bean
//    public ItemReader<PetInfo> itemReader() {
//        String cutoffDate = LocalDate.now().minusWeeks(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//
//        return new JpaPagingItemReaderBuilder<PetInfo>()
//                .name("jpaItemReader")
//                .entityManagerFactory(emf)
//                .queryString("SELECT p FROM PetInfo p WHERE p.happenDt < :cutoffDate")
//                .parameterValues(Map.of("cutoffDate", cutoffDate))
//                .pageSize(1000)
//                .build();
//    }
////    @Bean
////    public ItemReader<PetInfo> itemReader() {
////
////        return new CustomJpaPagingItemReader(entityManagerFactory);
////    }
//
//
//
//
//
//    /**
//     * Processor: 데이터 검증 또는 가공 (필요시 사용)
//     */
//    @Bean
//    public ItemProcessor<PetInfo, PetInfo> itemProcessor() {
//        return item -> {
//            // 필요 시 삭제 전 데이터 검증 로직 추가
//            System.out.println("Processing item: " + item);
//            return item;
//        };
//    }
//
//    /**
//     * Writer: 데이터 삭제 처리
//     */
//    @Bean
//    public ItemWriter<PetInfo> itemWriter() {
//        return items -> {
//
//                for (PetInfo item : items) {
//                    System.out.println("Deleting item: " + item); // 삭제 로그
//                    entityManager.remove(entityManager.contains(item) ? item : entityManager.merge(item));
//                    //entityManager.remove(entityManager.merge(item));
//                    System.out.println("after Deleting item: " + item.getId());
//                }
//                entityManager.getTransaction().commit();
//            };
//        }
//    }
//
//    /**
//     * Job 실행 상태 로그 및 알림 리스너
//     */
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final EntityManagerFactory emf;
    @PersistenceContext
    private final EntityManager entityManager;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final PetInfoRepository petInfoRepository;

    @Bean
    public org.springframework.batch.core.Job deleteOldRecordsJob(Step deleteStep) {
        return new JobBuilder("deleteOldRecordsJob", jobRepository)
                .start(deleteStep)
                .listener(new BatchListener()) // 리스너 추가 (필요시)
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

    /**
     * Processor: 데이터 검증 또는 가공 (필요시 사용)
     */
    @Bean
    public ItemProcessor<PetInfo, PetInfo> itemProcessor() {
        return item -> {
            // 필요 시 삭제 전 데이터 검증 로직 추가
            System.out.println("Processing item: " + item);
            return item;
        };
    }

    /**
     * Writer: 데이터 삭제 처리
     */
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
    /**
     * Job 실행 상태 로그 및 알림 리스너
     */
}
