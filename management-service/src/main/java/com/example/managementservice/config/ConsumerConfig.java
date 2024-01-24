package com.example.managementservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ConsumerConfig {
    @Value("${spring.kafka.topic.loans-approved.name}")
    private String loansApprovedTopic;

    @Bean
    public NewTopic loanApprovedTopic() {
        return TopicBuilder.name(loansApprovedTopic)
                .build();
    }
}
