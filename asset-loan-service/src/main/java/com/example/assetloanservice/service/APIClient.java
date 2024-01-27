package com.example.assetloanservice.service;

import com.example.assetloanservice.dto.HardwareDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(url = "http://localhost:8080", value = "MANAGEMENT-SERVICE")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public interface APIClient {

    @GetMapping("api/v1/hardware/asset/{assetCode}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    HardwareDto getHardwareDto(@PathVariable("assetCode") String assetCode);

    @PostMapping("api/products/validate-quantity")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    String validateQuantity(@RequestBody Map<String, Integer> detailsQuantityMap);
}
