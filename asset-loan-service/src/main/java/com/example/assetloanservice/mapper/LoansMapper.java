package com.example.assetloanservice.mapper;

import com.example.assetloanservice.dto.LoansDto;
import com.example.assetloanservice.entity.Loans;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LoansMapper {
    Loans toEntity(LoansDto loansDto);

    @AfterMapping
    default void linkDetails(@MappingTarget Loans loans) {
        loans.getDetails().forEach(detail -> detail.setLoans(loans));
    }

    LoansDto toDto(Loans loans);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Loans partialUpdate(LoansDto loansDto, @MappingTarget Loans loans);
}