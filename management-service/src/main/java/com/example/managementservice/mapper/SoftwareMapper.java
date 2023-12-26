package com.example.managementservice.mapper;

import com.example.managementservice.dto.SoftwareDto;
import com.example.managementservice.entity.Software;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SoftwareMapper {
    Software toEntity(SoftwareDto softwareDto);

    SoftwareDto toDto(Software software);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Software partialUpdate(SoftwareDto softwareDto, @MappingTarget Software software);
}