package com.example.managementservice.repository;

import com.example.managementservice.entity.Software;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SoftwareRepository extends JpaRepository<Software, Long> {
    List<Software> findByNameContainingIgnoreCase(String name);

    List<Software> findByVendorContainingIgnoreCase(String vendor);

    Optional<Software> findByAssetCodeContainingIgnoreCase(String assetCode);
}