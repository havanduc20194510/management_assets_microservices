package com.example.assetloanservice.dto;

import com.example.assetloanservice.Enum.LoanStatus;
import com.example.assetloanservice.entity.LoanDetails;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * DTO for {@link com.example.assetloanservice.entity.Loans}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoansDto implements Serializable {
    Long id;
    String username;
    Date startDate;
    Date dueDate;
    Date returnDate;
    LoanStatus status;
    List<LoanDetails> details;
}
