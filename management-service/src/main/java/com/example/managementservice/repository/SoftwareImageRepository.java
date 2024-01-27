package com.example.managementservice.repository;

import com.example.managementservice.entity.SoftwareImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SoftwareImageRepository extends JpaRepository<SoftwareImage, Long> {
    List<SoftwareImage> findBySoftwareId(Long softwareId);
}
