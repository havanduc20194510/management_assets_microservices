package com.example.managementservice.mapper;

import com.example.managementservice.dto.HardwareDto;
import com.example.managementservice.entity.Hardware;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface HardwareMapper {
    Hardware toEntity(HardwareDto hardwareDto);

    HardwareDto toDto(Hardware hardware);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Hardware partialUpdate(HardwareDto hardwareDto, @MappingTarget Hardware hardware);
}