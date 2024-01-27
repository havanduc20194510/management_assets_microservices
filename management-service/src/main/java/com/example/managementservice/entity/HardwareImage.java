package com.example.managementservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hardware_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HardwareImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long hardwareId;
    private String url;
}
