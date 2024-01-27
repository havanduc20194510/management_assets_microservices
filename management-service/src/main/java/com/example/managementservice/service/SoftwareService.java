package com.example.managementservice.service;

import com.example.managementservice.dto.SoftwareDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SoftwareService {
    SoftwareDto insertSoftware(SoftwareDto softwareDto, List<MultipartFile> images);
    SoftwareDto getSoftwareById(Long id);
    List<SoftwareDto> getAll();
    SoftwareDto updateSoftware(Long id, SoftwareDto softwareDto);
    String deleteSoftwareById(Long id);
    List<SoftwareDto> findByName(String name);
    List<SoftwareDto> findByVendor(String vendor);
    Page<SoftwareDto> getAllPaginated(int page, int size);
    SoftwareDto getSoftwareByAssetCode(String assetCode);
}
