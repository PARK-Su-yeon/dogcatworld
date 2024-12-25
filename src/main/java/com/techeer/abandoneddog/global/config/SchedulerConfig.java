package com.techeer.abandoneddog.global.config;

import com.techeer.abandoneddog.animal.service.PetInfoService;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class SchedulerConfig {

    private boolean isInitialized = false;
    private final PetInfoService petInfoService;

    // 생성자 주입
    public SchedulerConfig(PetInfoService petInfoService) {
        this.petInfoService = petInfoService;
    }

    // 10분마다 최근 10일 데이터 읽어오기
//    @Scheduled(fixedRate = 600000) // 10분 = 600,000ms
//    public void updateRecentPetInfo() {
//        petInfoService.updatePetInfoDaily();
//        System.out.println("최근 10일 데이터를 업데이트했습니다.");
//    }

    // 애플리케이션 시작 시 실행: 10일부터 300일까지 데이터 읽어오기
    @PostConstruct
    public void initializePastPetInfo() {
        if (!isInitialized) {
            petInfoService.update10To300DaysData();
            System.out.println("10일부터 300일까지 데이터를 초기화했습니다.");
            isInitialized = true;  // 초기화 완료 후 다시 호출되지 않도록 설정
        }
    }
}
