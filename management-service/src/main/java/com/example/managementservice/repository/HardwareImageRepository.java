package com.example.managementservice.repository;

import com.example.managementservice.entity.HardwareImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HardwareImageRepository extends JpaRepository<HardwareImage, Long> {
}
