package com.example.assetloanservice.service;

import com.example.assetloanservice.Enum.LoanStatus;
import com.example.assetloanservice.entity.Loans;
import com.example.assetloanservice.repository.LoansRepository;
import com.example.common_dto.dto.EventStatus;
import com.example.common_dto.event.InventoryCheckedEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InventoryConsumer {
    private LoansRepository loansRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryConsumer.class);
    @KafkaListener(
            topics = "${spring.kafka.topic.inventory-checked.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(InventoryCheckedEvent event) {
        LOGGER.info(String.format("received inventory check => %s", event));
        Long loanId = event.getLoanId();
        Loans loans = loansRepository.findById(loanId).get();
        boolean isInventoryOk = EventStatus.INVENTORY_CONFIRMED.equals(event.getStatus());
        LoanStatus status = isInventoryOk ? LoanStatus.APPROVED : LoanStatus.REJECTED;
        loans.setStatus(status);
        loansRepository.save(loans);
    }

}
