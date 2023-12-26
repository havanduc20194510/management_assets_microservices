package com.example.managementservice.service;

import com.example.managementservice.dto.HardwareDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HardwareService {
    Boolean insertHardware(HardwareDto hardwareDTO);
    boolean insertHardwareList(List<HardwareDto> hardwareDTOList);
    //    List<HardwareDTO> getAll();
    HardwareDto getHardwareById(Long id);
    String deleteHardwareById(Long id);
    List<HardwareDto> getHardwareByManufacturer(String name);
    String updateHardware(Long id, HardwareDto hardwareDTO) throws Exception;
    Page<HardwareDto> getAllPaginated(int page, int size);
    HardwareDto getHardwareByAssetCode(String assetCode);
}
