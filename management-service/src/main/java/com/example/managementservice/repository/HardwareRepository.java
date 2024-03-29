package com.example.managementservice.repository;

import com.example.managementservice.entity.Hardware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HardwareRepository extends JpaRepository<Hardware, Long> {
    Optional<Hardware> findByManufacturer(String name);

    Optional<Hardware> findByAssetCode(String assetCode);

    Page<Hardware> findAllByNameContainingOrDescriptionContainingOrManufacturerContainingOrLocationContaining(String name, String description, String manufacturer, String location, Pageable pageable);
}
