package com.example.managementservice.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.managementservice.dto.HardwareDto;
import com.example.managementservice.entity.Hardware;
import com.example.managementservice.entity.HardwareImage;
import com.example.managementservice.mapper.HardwareMapper;
import com.example.managementservice.repository.HardwareRepository;
import com.example.managementservice.repository.HardwareImageRepository;
import com.example.managementservice.service.HardwareService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HardwareServiceImpl implements HardwareService {
    private HardwareRepository hardwareRepository;
    private HardwareMapper hardwareMapper;
    private Cloudinary cloudinary;
    private HardwareImageRepository hardwareImageRepository;
    @Override
    public Boolean insertHardware(HardwareDto hardwareDTO, List<MultipartFile> images) {
        Hardware hardware = hardwareMapper.toEntity(hardwareDTO);
        Hardware savedHardware = hardwareRepository.save(hardware);
        // Image upload
        if (images == null) {
            return false;
        }
        images.forEach(image -> {
            try {
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                HardwareImage imageSaved = new HardwareImage();
                imageSaved.setUrl(uploadResult.get("url").toString());
                imageSaved.setHardwareId(savedHardware.getId());
                hardwareImageRepository.save(imageSaved);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return true;
    }

    @Override
    public boolean insertHardwareList(List<HardwareDto> hardwareDTOList) {
        for (HardwareDto hardwareDTO : hardwareDTOList) {
            Hardware hardware = hardwareMapper.toEntity(hardwareDTO);
            Optional<Hardware> existingHardwareOpt = hardwareRepository.findById(hardware.getId());

            if (existingHardwareOpt.isPresent()) {
                Hardware existingHardware = existingHardwareOpt.get();
                existingHardware.setQuantity(existingHardware.getQuantity() + 1);
                hardwareRepository.save(existingHardware);
            } else {
                hardware.setQuantity(1);
                hardwareRepository.save(hardware);
            }
        }
        return true;
    }

    @Override
    public HardwareDto getHardwareById(Long id) {
        Optional<Hardware> hardware = hardwareRepository.findById(id);
        return hardwareMapper.toDto(hardware.get());
    }

    @Override
    public String deleteHardwareById(Long id) {
        Optional<Hardware> hardware = hardwareRepository.findById(id);
        if (hardware.isPresent()) {
            hardwareRepository.deleteById(id);
            return "Successfully deleted hardware with id: " + id;
        }
        return "Hardware with id: " + id + " does not exist";
    }

    @Override
    public List<HardwareDto> getHardwareByManufacturer(String name) {
        List<Optional<Hardware>> list = Collections.singletonList(hardwareRepository.findByManufacturer(name));
        return list.stream()
                .map(hardware -> hardwareMapper.toDto(hardware.get()))
                .collect(Collectors.toList());
    }

    @Override
    public String updateHardware(Long id, HardwareDto hardwareDTO) {
        Optional<Hardware> existingHardware = hardwareRepository.findById(id);
        if (existingHardware.isPresent()) {
            Hardware hardware = existingHardware.get();
            hardware.setName(hardwareDTO.getName());
            hardware.setManufacturer(hardwareDTO.getManufacturer());
            hardware.setDescription(hardwareDTO.getDescription());
            hardware.setSerialNumber(hardwareDTO.getSerialNumber());
            hardware.setQuantity(hardwareDTO.getQuantity());
            hardware.setPurchaseValue(hardwareDTO.getPurchaseValue());
            hardwareRepository.save(hardware);
            return "Update successfully";
        } else {
            return "doesn't exist";
        }
    }

    @Override
    public Page<HardwareDto> getAllPaginated(int page, int size, String searchText) {
        if(searchText != null) {
            Pageable pageable = PageRequest.of(page-1, size);
            Page<Hardware> hardwarePage = hardwareRepository.findAllByNameContainingOrDescriptionContainingOrManufacturerContainingOrLocationContaining(searchText, searchText, searchText, searchText, pageable);
            List<HardwareDto> hardwareDTOList = hardwarePage.stream()
                    .map(hardware -> hardwareMapper.toDto(hardware))
                    .collect(Collectors.toList());
            return new PageImpl<>(hardwareDTOList, pageable, hardwarePage.getTotalElements());
        }
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Hardware> hardwarePage = hardwareRepository.findAll(pageable);
        List<HardwareDto> hardwareDTOList = hardwarePage.stream()
                .map(hardware -> hardwareMapper.toDto(hardware))
                .collect(Collectors.toList());
        return new PageImpl<>(hardwareDTOList, pageable, hardwarePage.getTotalElements());
    }

    @Override
    public HardwareDto getHardwareByAssetCode(String assetCode) {
        Optional<Hardware> hardware = hardwareRepository.findByAssetCode(assetCode);
        return hardwareMapper.toDto(hardware.get());
    }
}
