package com.example.assetloanservice.entity;

import com.example.assetloanservice.Enum.LoanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "loans")
public class Loans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String username;
    private String assetCode;
    private Date startDate;
    private Date dueDate;
    private Date returnDate;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
}