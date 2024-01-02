package com.example.assetloanservice.dto;

import com.example.assetloanservice.Enum.LoanStatus;
import lombok.Value;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * DTO for {@link com.example.assetloanservice.entity.Loans}
 */
@Value
public class LoansDto implements Serializable {
    Long id;
    String username;
    Date startDate;
    Date dueDate;
    Date returnDate;
    LoanStatus status;
    List<LoanDetailsDto> details;
}