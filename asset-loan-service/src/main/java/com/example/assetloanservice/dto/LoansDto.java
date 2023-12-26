package com.example.assetloanservice.dto;

import com.example.assetloanservice.Enum.LoanStatus;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

/**
 * DTO for {@link com.example.assetloanservice.entity.Loans}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoansDto implements Serializable {
    String username;
    String assetCode;
    Date startDate;
    Date dueDate;
    Date returnDate;
    LoanStatus status;
}