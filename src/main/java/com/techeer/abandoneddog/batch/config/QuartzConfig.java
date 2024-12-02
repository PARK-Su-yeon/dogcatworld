package com.techeer.abandoneddog.batch.config;

import com.techeer.abandoneddog.batch.QuartzJobLauncher;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail deleteOldRecordsJobDetail() {
        return JobBuilder.newJob(QuartzJobLauncher.class)
                .withIdentity("deleteOldRecordsJob")
                .storeDurably() // 트리거가 없는 경우에도 Job이 유지되도록 설정
                .build();
    }

    @Bean
    public Trigger deleteOldRecordsTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(deleteOldRecordsJobDetail())
                .withIdentity("deleteOldRecordsTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(1) // 3분 간격
                        .repeatForever())
               // .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 ? * MON#2")) // 매달 둘째 월요일 자정 실행
                .build();
    }
}
