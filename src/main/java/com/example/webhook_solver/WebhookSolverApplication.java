package com.example.webhooksolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.webhooksolver.dto.WebhookResponse;
import com.example.webhooksolver.service.WebhookService;

@SpringBootApplication
public class WebhookSolverApplication implements CommandLineRunner {

    @Autowired
    private WebhookService webhookService;

    public static void main(String[] args) {
        SpringApplication.run(WebhookSolverApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting webhook generation...");
        
        try {
            // Step 1: Generate webhook
            WebhookResponse response = webhookService.generateWebhook();
            System.out.println("Webhook URL: " + response.getWebhook());
            System.out.println("Access Token: " + response.getAccessToken());
            
            // Step 2: Solve SQL problem based on regNo
            String regNo = "REG12347"; // Replace with your actual regNo
            String sqlQuery = webhookService.solveSqlProblem(regNo);
            System.out.println("Generated SQL Query: " + sqlQuery);
            
            // Step 3: Submit solution
            webhookService.submitSolution(response.getWebhook(), response.getAccessToken(), sqlQuery);
            
            System.out.println("Process completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}