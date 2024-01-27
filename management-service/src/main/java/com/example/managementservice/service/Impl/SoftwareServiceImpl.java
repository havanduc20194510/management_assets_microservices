package com.example.managementservice.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.managementservice.dto.SoftwareDto;
import com.example.managementservice.entity.HardwareImage;
import com.example.managementservice.entity.Software;
import com.example.managementservice.entity.SoftwareImage;
import com.example.managementservice.mapper.SoftwareMapper;
import com.example.managementservice.repository.SoftwareImageRepository;
import com.example.managementservice.repository.SoftwareRepository;
import com.example.managementservice.service.SoftwareService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SoftwareServiceImpl implements SoftwareService {
    private SoftwareRepository softwareRepository;
    private SoftwareMapper softwareMapper;
    private Cloudinary cloudinary;
    private final SoftwareImageRepository softwareImageRepository;

    @Override
    public SoftwareDto insertSoftware(SoftwareDto softwareDto, List<MultipartFile> images) {
        Software sofware = softwareMapper.toEntity(softwareDto);
        Software sofwareSaved = softwareRepository.save(sofware);
        SoftwareDto softwareDtoSaved = softwareMapper.toDto(sofwareSaved);
        // Image upload
        if (images == null) {
            return null;
        }
        images.forEach(image -> {
            try {
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                SoftwareImage imageSaved = new SoftwareImage();
                imageSaved.setUrl(uploadResult.get("url").toString());
                imageSaved.setSoftwareId(softwareDto.getId());
                softwareImageRepository.save(imageSaved);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        List<SoftwareImage> softwareImages = softwareImageRepository.findBySoftwareId(softwareDto.getId());
        softwareDtoSaved.setImages(softwareImages);
        return softwareDtoSaved;
    }

    @Override
    public SoftwareDto getSoftwareById(Long id) {
        Optional<Software> sofware = softwareRepository.findById(id);
        if (!sofware.isPresent()) {
            return null;
        }
        SoftwareDto softwareDto = softwareMapper.toDto(sofware.get());
        return softwareDto;
    }

    @Override
    public List<SoftwareDto> getAll() {
        List<Software> sofwareList = softwareRepository.findAll();

        return sofwareList.stream()
                .map(software -> softwareMapper.toDto(software))
                .collect(Collectors.toList());
    }

    @Override
    public SoftwareDto updateSoftware(Long id, SoftwareDto softwareDto) {
        Optional<Software> sofware = softwareRepository.findById(id);
        if (!sofware.isPresent()) {
            return null;
        }
        Date updateDate = new Date();
        Software softwareToUpdate = sofware.get();
        softwareToUpdate.setName(softwareDto.getName());
        softwareToUpdate.setVendor(softwareDto.getVendor());
        softwareToUpdate.setVersion(softwareDto.getVersion());
        softwareToUpdate.setLicenseKey(softwareDto.getLicenseKey());
        softwareToUpdate.setDescription(softwareDto.getDescription());
        softwareToUpdate.setPricePerSeat(softwareDto.getPricePerSeat());
        softwareToUpdate.setSeats(softwareDto.getSeats());
        softwareToUpdate.setCreateDate(softwareDto.getCreateDate());
        softwareToUpdate.setUpdateDate(updateDate);
        Software sofwareUpdated = softwareRepository.save(softwareToUpdate);
        SoftwareDto softwareDtoUpdated = softwareMapper.toDto(sofwareUpdated);
        return softwareDtoUpdated;
    }

    @Override
    public String deleteSoftwareById(Long id) {
        if (softwareRepository.existsById(id)) {
            softwareRepository.deleteById(id);
            return "Software with ID " + id + " was deleted successfully.";
        }
        return "Software with ID " + id + " not found.";
    }

    @Override
    public List<SoftwareDto> findByName(String name) {
        List<Software> softwares = softwareRepository.findByNameContainingIgnoreCase(name);
        return softwares.stream()
                .map(software -> softwareMapper.toDto(software))
                .collect(Collectors.toList());
    }

    @Override
    public List<SoftwareDto> findByVendor(String vendor) {
        List<Software> softwares = softwareRepository.findByVendorContainingIgnoreCase(vendor);
        return softwares.stream()
                .map(software -> softwareMapper.toDto(software))
                .collect(Collectors.toList());
    }

    @Override
    public Page<SoftwareDto> getAllPaginated(int page, int size) {
        // Xây dựng đối tượng Pageable để xác định trang và kích thước trang
        Pageable pageable = PageRequest.of(page-1, size);

        // Truy vấn dữ liệu từ cơ sở dữ liệu bằng Spring Data JPA
        Page<Software> softwarePage = softwareRepository.findAll(pageable);

        // Chuyển đổi danh sách các đối tượng Entity sang DTO (Data Transfer Object)
        List<SoftwareDto> softwareDtoList = softwarePage.getContent().stream()
                .map(software -> softwareMapper.toDto(software)) // Sử dụng một mapper để chuyển đổi
                .collect(Collectors.toList());

        // Trả về trang kết quả đã chuyển đổi thành DTO
        return new PageImpl<>(softwareDtoList, pageable, softwarePage.getTotalElements());

    }

    @Override
    public SoftwareDto getSoftwareByAssetCode(String assetCode) {
        Optional<Software> sofware = softwareRepository.findByAssetCodeContainingIgnoreCase(assetCode);
        return softwareMapper.toDto(sofware.get());
    }
}
