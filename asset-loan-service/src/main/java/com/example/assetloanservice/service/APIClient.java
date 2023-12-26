package com.example.assetloanservice.service;

import com.example.assetloanservice.dto.AssetDTO;
import com.example.assetloanservice.dto.HardwareDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:8080", value = "MANAGEMENT-SERVICE")
public interface APIClient {
    @GetMapping("api/products/{assetCode}")
    AssetDTO getAssetDTO(@PathVariable("assetCode") String assetCode);

    @GetMapping("api/v1/hardware/asset/{assetCode}")
    HardwareDto getHardwareDto(@PathVariable("assetCode") String assetCode);
}
