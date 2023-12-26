package com.example.managementservice.service.Impl;

import com.example.managementservice.dto.AssetDTO;
import com.example.managementservice.entity.Hardware;
import com.example.managementservice.entity.Software;
import com.example.managementservice.mapper.AssetMapper;
import com.example.managementservice.repository.HardwareRepository;
import com.example.managementservice.repository.SoftwareRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private HardwareRepository hardwareRepository;
    private SoftwareRepository softwareRepository;


    public AssetDTO getProductByAssetCode(String assetCode) {
        Optional<Hardware> hardware = hardwareRepository.findByAssetCode(assetCode);
        if (hardware.isPresent()) {
            return AssetMapper.toDTO(hardware.get());
        }
        Optional<Software> software = softwareRepository.findByAssetCodeContainingIgnoreCase(assetCode);
        if (software.isPresent()) {
            return AssetMapper.toDTO(software.get());
        }
        return null;
    }
}
