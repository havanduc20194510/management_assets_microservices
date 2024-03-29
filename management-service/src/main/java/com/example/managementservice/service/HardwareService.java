package com.example.managementservice.service;

import com.example.managementservice.dto.HardwareDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HardwareService {
    Boolean insertHardware(HardwareDto hardwareDTO, List<MultipartFile> files);
    boolean insertHardwareList(List<HardwareDto> hardwareDTOList);
    //    List<HardwareDTO> getAll();
    HardwareDto getHardwareById(Long id);
    String deleteHardwareById(Long id);
    List<HardwareDto> getHardwareByManufacturer(String name);
    String updateHardware(Long id, HardwareDto hardwareDTO) throws Exception;
    Page<HardwareDto> getAllPaginated(int page, int size, String searchText);
    HardwareDto getHardwareByAssetCode(String assetCode);
}
