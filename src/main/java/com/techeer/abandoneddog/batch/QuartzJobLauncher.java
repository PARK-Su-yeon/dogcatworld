package com.techeer.abandoneddog.batch;


import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuartzJobLauncher implements Job {

    private final JobLauncher jobLauncher;
    private final org.springframework.batch.core.Job deleteOldRecordsJob;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(deleteOldRecordsJob, params);
            System.out.println("Batch job triggered successfully!");
        } catch (Exception e) {
            System.err.println("Error triggering batch job: " + e.getMessage());
        }
    }
}
