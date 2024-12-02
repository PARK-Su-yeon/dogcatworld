package com.techeer.abandoneddog.batch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class BatchListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Starting Job: " + jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            System.err.println("Job failed: " + jobExecution.getJobInstance().getJobName());
            sendAlert("Batch job failed: " + jobExecution.getJobInstance().getJobName());
        } else {
            System.out.println("Job completed successfully: " + jobExecution.getJobInstance().getJobName());
        }
    }

    private void sendAlert(String message) {
        // 알림 전송
        System.out.println("Alert sent: " + message);
    }
}