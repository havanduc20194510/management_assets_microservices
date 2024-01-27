package com.example.assetloanservice.dto;

import com.example.assetloanservice.Enum.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanDetailResponseDto {
    Long id;
    String username;
    Date startDate;
    Date dueDate;
    Date returnDate;
    LoanStatus status;
    private List<HardwareDto> details;
}
