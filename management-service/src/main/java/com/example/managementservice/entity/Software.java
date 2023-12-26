package com.example.managementservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "software", indexes = {
        @Index(name = "idx_software_asset_code", columnList = "asset_code")
})
@DynamicInsert
@DynamicUpdate
public class Software {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private String assetCode;
    private String version;
    private String licenseKey;
    private Integer seats;
    private Double pricePerSeat;
    private String vendor;
    private String description;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
}