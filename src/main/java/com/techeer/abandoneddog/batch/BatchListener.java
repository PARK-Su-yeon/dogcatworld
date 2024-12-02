package com.techeer.abandoneddog.batch;

import com.techeer.abandoneddog.batch.monitor.SlackNotifier;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class BatchListener implements JobExecutionListener {
    private final SlackNotifier slackNotifier;
    @Autowired
    public BatchListener(SlackNotifier slackNotifier) {
        this.slackNotifier = slackNotifier;
    }


    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Starting Job: " + jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        String jobName = jobExecution.getJobInstance().getJobName();
        String status = jobExecution.getStatus().toString();

        String message = String.format("batch job'%s' state: %s", jobName, status);
        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            System.err.println("Job 실패: " + jobExecution.getJobInstance().getJobName());
            slackNotifier.notify(message);


        } else {
            slackNotifier.notify(message);
            System.out.println("Job completed successfully: " + jobExecution.getJobInstance().getJobName());
        }
    }


}
