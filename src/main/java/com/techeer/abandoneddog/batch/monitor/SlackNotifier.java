package com.techeer.abandoneddog.batch.monitor;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SlackNotifier {

    @Value("${slack.webhook.url}")
    private String webhookUrl;

    public void onReadError(Exception ex) {
        notify("Error occurred during batch processing: " + ex.getMessage());
    }

    public void onStepError(String stepName, Exception ex) {
        notify("Error occurred in step " + stepName + ": " + ex.getMessage());
    }
    public void messag(String message) {
        restTemplate.postForObject(webhookUrl, message, String.class);
    }
    private final RestTemplate restTemplate = new RestTemplate();

    public void notify(String message) {
        try {
            JSONObject payload = new JSONObject();
            payload.put("text", message);

            // JSON 형식으로 메시지 전송
            restTemplate.postForObject(webhookUrl, payload.toString(), String.class);
        } catch (Exception e) {
            // 예외 처리
            System.out.println("Error notifying Slack: " + e.getMessage());
        }
    }
}
