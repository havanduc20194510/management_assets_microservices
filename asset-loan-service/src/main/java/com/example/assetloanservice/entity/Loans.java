package com.example.assetloanservice.entity;

import com.example.assetloanservice.Enum.LoanStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

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
    private Date startDate;
    private Date dueDate;
    private Date returnDate;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
    @OneToMany(mappedBy = "loans", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LoanDetails> details;
}