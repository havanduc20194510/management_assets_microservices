package com.example.managementservice.service.Impl;

import com.example.managementservice.dto.AssetDTO;
import com.example.managementservice.dto.HardwareDto;
import com.example.managementservice.entity.Hardware;
import com.example.managementservice.entity.Software;
import com.example.managementservice.mapper.AssetMapper;
import com.example.managementservice.repository.HardwareRepository;
import com.example.managementservice.repository.SoftwareRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
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
        return software.map(AssetMapper::toDTO).orElse(null);
    }

    // method để lấy ra số lượng hàng tồn kho
    // method để validate số lượng trong kho và đơn mượn
    public String validateQuantity(Map<String, Integer> detailsQuantityMap) {
        for (String assetCode : detailsQuantityMap.keySet()) {
            Hardware hardware = hardwareRepository.findByAssetCode(assetCode).get();
            int quantity = hardware.getQuantity();
            if (!(quantity > 0)) {
                return "The warehouse is out of products ...";
            }
            if (!(detailsQuantityMap.get(assetCode) < quantity)) {
                return "Not enough products ...";
            }
        }
        return null;
    }
}
