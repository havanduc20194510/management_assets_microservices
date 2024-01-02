package com.example.assetloanservice.repository;

import com.example.assetloanservice.entity.LoanDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanDetailsRepository extends JpaRepository<LoanDetails, Long> {
    List<LoanDetails> findByLoansId(Long loanId);
}