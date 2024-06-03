package com.techeer.abandoneddog.global.config;

import com.techeer.abandoneddog.animal.service.PetInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private PetInfoService petInfoService;

    // 매일 자정에 실행되는 스케줄링 메서드
    @Scheduled(fixedRate = 600000)
    public void schedulePetInfoUpdate() {
        petInfoService.updatePetInfoDaily();
    }
}
