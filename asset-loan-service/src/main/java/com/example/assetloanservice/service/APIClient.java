package com.example.assetloanservice.service;

import com.example.assetloanservice.dto.HardwareDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(url = "http://localhost:8080", value = "MANAGEMENT-SERVICE")
public interface APIClient {

    @GetMapping("api/v1/hardware/asset/{assetCode}")
    HardwareDto getHardwareDto(@PathVariable("assetCode") String assetCode);

    @PostMapping("api/products/validate-quantity")
    String validateQuantity(@RequestBody Map<String, Integer> detailsQuantityMap);
}
