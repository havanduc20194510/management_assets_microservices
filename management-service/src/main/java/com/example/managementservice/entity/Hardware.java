package com.example.managementservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hardware", indexes = {
        @Index(name = "idx_hardware_asset_code", columnList = "asset_code")
})
@DynamicUpdate
@DynamicInsert
public class Hardware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable  = false)
    private String name;
    private int quantity;
    @Column(nullable = false)
    private String manufacturer;
    private String description;
    @Column(nullable = false)
    private String assetCode;
    @Column(nullable = false)
    private String serialNumber;
    private String location;
    @Column(nullable = false)
    private Long purchaseValue;
    @Column(nullable = false)
    private Date purchaseDate;
}