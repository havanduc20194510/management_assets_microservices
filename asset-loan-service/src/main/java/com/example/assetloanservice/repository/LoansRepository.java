package com.example.assetloanservice.repository;

import com.example.assetloanservice.entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoansRepository extends JpaRepository<Loans, Long> {

}