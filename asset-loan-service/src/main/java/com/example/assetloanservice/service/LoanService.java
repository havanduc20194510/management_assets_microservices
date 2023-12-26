package com.example.assetloanservice.service;

import com.example.assetloanservice.dto.APIResponseDTO;
import com.example.assetloanservice.dto.LoansDto;
import org.springframework.data.domain.Page;



public interface LoanService {
    APIResponseDTO getLoanDetails(Long loanId);
    Page<LoansDto> getApproveLoansList(int page, int size);

    Page<LoansDto> getReturnedLoansList(int page, int size);

    String rejectOrder(Long id);
}
