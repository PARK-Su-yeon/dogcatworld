package com.techeer.abandoneddog.batch;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.batch.monitor.SlackNotifier;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BatchListener  implements JobExecutionListener, StepExecutionListener{
    private final SlackNotifier slackNotifier;
    private long startTime;

    @Autowired
    public BatchListener(SlackNotifier slackNotifier) {
        this.slackNotifier = slackNotifier;
    }


    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime =System.currentTimeMillis();
        slackNotifier.notify("BEFORE JOB START");
        System.out.println("Starting Job: " + jobExecution.getJobInstance().getJobName());
    }


    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // Step 처리 결과 기록
        slackNotifier.notify("Step "+ stepExecution.getStepName());
        slackNotifier.notify("Read Count: {}"+ stepExecution.getReadCount());
        slackNotifier.notify("Write Count: {}"+ stepExecution.getWriteCount());
        slackNotifier.notify("Skip Count: {}"+stepExecution.getSkipCount());
        slackNotifier.notify("Read Skip Count: {}"+ stepExecution.getReadSkipCount());
        slackNotifier.notify("Write Skip Count: {}"+ stepExecution.getWriteSkipCount());
        slackNotifier.notify("Process Skip Count: {}"+ stepExecution.getProcessSkipCount());
        slackNotifier.notify("Commit Count: {}"+ stepExecution.getCommitCount());
        slackNotifier.notify("Rollback Count: {}"+ stepExecution.getRollbackCount());
      //  slackNotifier.notify("Total Time Taken: {}ms"+stepExecution.getEndTime() - stepExecution.getStartTime());


        if (stepExecution.getStatus().isUnsuccessful()) {
            // 실패한 경우 Slack에 오류 알림
            slackNotifier.onStepError(stepExecution.getStepName(), new Exception("Step execution failed"));
        }
        else         slackNotifier.notify("Step " + stepExecution.getStepName() + " completed successfully.");

        // 성공적인 경우에는 추가적인 처리가 필요할 경우 여기서 수행
        return null;
    }
    @Override
    public void afterJob(JobExecution jobExecution) {

        String jobName = jobExecution.getJobInstance().getJobName();
        String status = jobExecution.getStatus().toString();
        //로그
        long endTime = System.currentTimeMillis();
        System.out.println("Batch Job Completed: " + jobExecution.getJobInstance().getJobName());
        System.out.println("Total Time Taken: " + (endTime - startTime) + "ms");
        System.out.println("Job Status: " + jobExecution.getStatus());

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
