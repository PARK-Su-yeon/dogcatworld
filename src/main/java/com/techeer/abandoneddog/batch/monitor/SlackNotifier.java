package com.techeer.abandoneddog.batch.monitor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SlackNotifier {

    @Value("${slack.webhook.url}")
    private String webhookUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void notify(String message) {
        String payload = String.format("{\"text\": \"%s\"}", message);
        restTemplate.postForObject(webhookUrl, payload, String.class);
    }
}
