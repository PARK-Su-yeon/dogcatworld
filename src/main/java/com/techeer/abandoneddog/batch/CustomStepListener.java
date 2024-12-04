package com.techeer.abandoneddog.batch;

import com.techeer.abandoneddog.batch.monitor.SlackNotifier;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class CustomStepListener extends StepExecutionListenerSupport {
    private final SlackNotifier slackNotifier;

    public CustomStepListener(SlackNotifier slackNotifier) {
        this.slackNotifier = slackNotifier;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getStatus().isUnsuccessful()) {
            System.err.println("Step failed: " + stepExecution.getStepName());
            System.err.println("Failure exceptions:");

            stepExecution.getFailureExceptions().forEach(ex -> {
                System.err.println(" - Exception: " + ex.getMessage());
                ex.printStackTrace();
            });

            // Slack으로 알림
            String errorMessage = String.format("Step '%s' failed with errors: %s",
                    stepExecution.getStepName(),
                    stepExecution.getFailureExceptions().stream()
                            .map(Throwable::getMessage)
                            .reduce("", (a, b) -> a + "\n" + b)
            );
            slackNotifier.notify(errorMessage);
        }
        return stepExecution.getExitStatus();
    }
}