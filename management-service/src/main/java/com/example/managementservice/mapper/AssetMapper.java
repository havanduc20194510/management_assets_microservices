package com.example.managementservice.mapper;

import com.example.managementservice.dto.AssetDTO;
import com.example.managementservice.entity.Hardware;
import com.example.managementservice.entity.Software;
import org.springframework.stereotype.Component;

@Component
public class AssetMapper {
    public static AssetDTO toDTO(Hardware hardware) {
        AssetDTO dto = new AssetDTO();
        dto.setId(hardware.getId());
        dto.setAssetName(hardware.getName());
        dto.setAssetDescription(hardware.getDescription());
        dto.setAssetCode(hardware.getAssetCode());
        return dto;
    }

    public static AssetDTO toDTO(Software software) {
        AssetDTO dto = new AssetDTO();
        dto.setId(software.getId());
        dto.setAssetName(software.getName());
        dto.setAssetDescription(software.getDescription());
        dto.setAssetCode(software.getAssetCode());
        return dto;
    }
}
