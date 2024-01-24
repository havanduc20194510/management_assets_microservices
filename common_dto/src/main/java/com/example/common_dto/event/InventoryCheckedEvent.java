package com.example.common_dto.event;

import com.example.common_dto.dto.EventStatus;
import com.example.common_dto.dto.LoanHardwareDto;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class InventoryCheckedEvent implements Event{
    private UUID eventId = UUID.randomUUID();
    private Date eventDate = new Date();
    private Long loanId;
    private List<LoanHardwareDto> list;
    private EventStatus status;
}
