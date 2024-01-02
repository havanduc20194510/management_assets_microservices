package com.example.assetloanservice.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.assetloanservice.entity.LoanDetails}
 */
@Value
public class LoanDetailsDto implements Serializable {
    String assetCode;
    int quantity;
    LoansDto loans;
}