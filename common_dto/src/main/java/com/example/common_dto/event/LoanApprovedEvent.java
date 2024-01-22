package com.example.common_dto.event;
import com.example.common_dto.dto.LoanDetailsDto;
import com.example.common_dto.dto.EventStatus;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoanApprovedEvent implements Event{
    private UUID eventId = UUID.randomUUID();
    private Date eventDate = new Date();
    private LoanDetailsDto details;
    private EventStatus status;

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Date getDate() {
        return eventDate;
    }
}
