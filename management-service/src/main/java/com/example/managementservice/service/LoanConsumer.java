package com.example.managementservice.service;

import com.example.common_dto.dto.EventStatus;
import com.example.common_dto.dto.LoanDetailsDto;
import com.example.common_dto.dto.LoanHardwareDto;
import com.example.common_dto.event.InventoryCheckedEvent;
import com.example.common_dto.event.LoanApprovedEvent;
import com.example.managementservice.service.Impl.ProductService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoanConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanConsumer.class);
    private ProductService productService;
    private NewTopic inventoryCheckedTopic;
    private KafkaTemplate<String, InventoryCheckedEvent> kafkaTemplate;

    @KafkaListener(
            topics = "${spring.kafka.topic.loans-approved.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ConsumerRecord<String, LoanApprovedEvent> record) {
        try {
            LoanApprovedEvent event = record.value();
            if (event == null) {
                throw  new RuntimeException(("Empty event object"));
            }
            if (event.getDetails() == null) {
                LOGGER.warn("Event details missing");
            }
            InventoryCheckedEvent checkedEvent = processEvent(event);
            LOGGER.info(String.format("InventoryCheckedEvent => %s", checkedEvent));
            Message<InventoryCheckedEvent> message = MessageBuilder
                    .withPayload(checkedEvent)
                    .setHeader(KafkaHeaders.TOPIC, inventoryCheckedTopic.name())
                    .build();
            kafkaTemplate.send(message);
        } catch (Exception e) {
            LOGGER.error("Error when consuming evemt", e);
        }
    }


    public InventoryCheckedEvent processEvent(LoanApprovedEvent event) {
        LOGGER.info("Process event: " + event);
        InventoryCheckedEvent response = null;
        try {
            LoanDetailsDto loanDetails = event.getDetails();
            List<LoanHardwareDto> outOfStock = productService.checkStockQuantity(loanDetails);
            response = new InventoryCheckedEvent();
            response.setLoanId(event.getDetails().getLoanId());
            if (!outOfStock.isEmpty()) {
                response.setStatus(EventStatus.INVENTORY_REJECTED);
                response.setList(outOfStock);
            } else {
                productService.updateStockQuantity(event.getDetails());
                response.setStatus(EventStatus.INVENTORY_CONFIRMED);
            }

        } catch (Exception e) {
            LOGGER.error("Error while processing event ", e);
        }
        return response;
    }
    @Bean
    public ConsumerAwareListenerErrorHandler errorHandler() {
        return ((message, exception, consumer) -> {
            LOGGER.error("Error in consumer config: " +
                    message.getPayload(), exception);
            return null;
        });
    }
}
