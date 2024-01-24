package com.example.managementservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class InventoryPublisherConfig {
    @Value("${spring.kafka.topic.inventory-checked.name}")
    private String InventoryCheckedTopic;

    @Bean
    public NewTopic inventoryCheckedTopic() {
        return TopicBuilder.name(InventoryCheckedTopic)
                .build();
    }
}
