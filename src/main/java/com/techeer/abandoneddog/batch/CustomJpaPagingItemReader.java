package com.techeer.abandoneddog.batch;

import com.querydsl.jpa.impl.JPAQuery;
import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.animal.entity.QPetInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CustomJpaPagingItemReader extends JpaPagingItemReader<PetInfo> {

    private final EntityManagerFactory entityManagerFactory; // EntityManagerFactory 사용
    private final String cutoffDate;

    public CustomJpaPagingItemReader(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.cutoffDate = LocalDate.now().minusWeeks(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    @Override
    protected void doReadPage() {
        EntityManager entityManager = entityManagerFactory.createEntityManager(); // EntityManager 생성
        try {
            QPetInfo petInfo = QPetInfo.petInfo;

            JPAQuery<PetInfo> query = new JPAQuery<>(entityManager);
            query.from(petInfo)
                    .where(petInfo.happenDt.lt(cutoffDate))
                    .offset(getPage() * getPageSize()) // 현재 페이지 오프셋 설정
                    .limit(getPageSize()); // 페이지 크기 설정

            if (results == null) {
                results = new ArrayList<>();
            } else {
                results.clear();
            }
            results.addAll(query.fetch());
        } finally {
            entityManager.close(); // EntityManager 닫기
        }
    }
}
