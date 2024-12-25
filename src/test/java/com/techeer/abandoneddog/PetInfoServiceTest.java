package com.techeer.abandoneddog;
import com.techeer.abandoneddog.animal.service.PetInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class PetInfoServiceTest {

    @Autowired
    private PetInfoService petInfoService;

    @Test
    public void testUpdate10To300DaysDataExecutionTime() {
        //log.info("Starting update10To300DaysData test...");

        // 시작 시간
        long startTime = System.currentTimeMillis();

        // 300일치 데이터를 삽입하는 메서드 실행
        petInfoService.update10To300DaysData();

        // 끝 시간
        long endTime = System.currentTimeMillis();

        // 소요 시간 계산
        long duration = endTime - startTime;

        //log.info("Execution time for update10To300DaysData: {} ms", duration);
        System.out.println("Execution time: " + duration + " ms");
    }


}
