package com.example.managementservice.service.Impl;

import com.example.common_dto.dto.LoanDetailsDto;
import com.example.common_dto.dto.LoanHardwareDto;
import com.example.managementservice.dto.AssetDTO;
import com.example.managementservice.entity.Hardware;
import com.example.managementservice.entity.Software;
import com.example.managementservice.mapper.AssetMapper;
import com.example.managementservice.repository.HardwareRepository;
import com.example.managementservice.repository.SoftwareRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    // hàm cập nhật số lượng
    @Transactional
    public void updateStockQuantity(LoanDetailsDto detailsDto) {
        List<LoanHardwareDto> loanHardwareList = detailsDto.getProducts();
        for (LoanHardwareDto loanHardwareDto : loanHardwareList) {
            Hardware hardware = hardwareRepository.findByAssetCode(loanHardwareDto.getAssetCode()).get();
            int newQuantity = hardware.getQuantity() - loanHardwareDto.getQuantity();
            hardware.setQuantity(newQuantity);
            hardwareRepository.save(hardware);
        }
    }

    public List<LoanHardwareDto> checkStockQuantity(LoanDetailsDto loanDetails) {
        List<LoanHardwareDto> outOfStockProducts = new ArrayList<>();
        List<LoanHardwareDto> loanProducts = loanDetails.getProducts();
        for(LoanHardwareDto loanProduct : loanProducts) {
            // Lấy thông tin sản phẩm từ database
            Hardware product = hardwareRepository.findByAssetCode(loanProduct.getAssetCode()).get();
            if(product.getQuantity() < loanProduct.getQuantity()) {
                outOfStockProducts.add(loanProduct);
            }
        }
        return outOfStockProducts;
    }

}
