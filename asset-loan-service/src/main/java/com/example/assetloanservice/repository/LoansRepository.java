package com.example.assetloanservice.repository;

import com.example.assetloanservice.entity.Loans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoansRepository extends JpaRepository<Loans, Long> {
    Page<Loans> findAllByUsernameOrderByStartDateDesc(String username, Pageable pageable);
}
