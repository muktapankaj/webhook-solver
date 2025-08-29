package com.example.webhooksolver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.webhooksolver.dto.WebhookRequest;
import com.example.webhooksolver.dto.WebhookResponse;
import com.example.webhooksolver.dto.SolutionRequest;

@Service
public class WebhookService {

    @Value("${api.generate-webhook}")
    private String generateWebhookUrl;

    @Value("${api.submit-solution}")
    private String submitSolutionUrl;

    @Value("${student.name}")
    private String studentName;

    @Value("${student.regNo}")
    private String studentRegNo;

    @Value("${student.email}")
    private String studentEmail;

    private final RestTemplate restTemplate;

    public WebhookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WebhookResponse generateWebhook() {
        WebhookRequest request = new WebhookRequest(studentName, studentRegNo, studentEmail);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(
            generateWebhookUrl, entity, WebhookResponse.class);
        
        return response.getBody();
    }

    public void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        SolutionRequest request = new SolutionRequest(sqlQuery);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        HttpEntity<SolutionRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            webhookUrl, entity, String.class);
        
        System.out.println("Submission Response: " + response.getBody());
    }

    public String solveSqlProblem(String regNo) {
        // Extract last two digits of registration number
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastDigits = Integer.parseInt(lastTwoDigits);
        
        // Determine if odd or even
        if (lastDigits % 2 == 1) {
            // Odd - Question 1
            return "SELECT * FROM employees WHERE department = 'IT' AND salary > 50000;";
        } else {
            // Even - Question 2
            return "SELECT name, age FROM customers WHERE city = 'Bangalore' ORDER BY name ASC;";
        }
    }
}