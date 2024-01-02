package com.example.assetloanservice.mapper.Details;

import com.example.assetloanservice.dto.LoanDetailsDto;
import com.example.assetloanservice.entity.LoanDetails;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LoanDetailsMapper {
    LoanDetails toEntity(LoanDetailsDto loanDetailsDto);

    LoanDetailsDto toDto(LoanDetails loanDetails);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LoanDetails partialUpdate(LoanDetailsDto loanDetailsDto, @MappingTarget LoanDetails loanDetails);
}