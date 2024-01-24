package com.example.assetloanservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class LoanPublisherConfig {
    @Value("${spring.kafka.topic.loan-approved.name}")
    private String loanApprovedTopic;

    @Bean
    public NewTopic loanApprovedTopic() {
        return TopicBuilder.name(loanApprovedTopic)
                .build();
    }
}
