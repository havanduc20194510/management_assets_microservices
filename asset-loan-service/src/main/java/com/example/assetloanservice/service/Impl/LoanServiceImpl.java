package com.example.assetloanservice.service.Impl;

import com.example.assetloanservice.Enum.LoanStatus;
import com.example.assetloanservice.dto.APIResponseDTO;
import com.example.assetloanservice.dto.HardwareDto;
import com.example.assetloanservice.dto.LoansDto;
import com.example.assetloanservice.entity.Loans;
import com.example.assetloanservice.mapper.LoansMapper;
import com.example.assetloanservice.repository.LoansRepository;
import com.example.assetloanservice.service.APIClient;
import com.example.assetloanservice.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {
    private LoansRepository loansRepository;
    private LoansMapper loansMapper;
    private APIClient apiClient;


    @Override
    public APIResponseDTO getLoanDetails(Long loanId) {
        Optional<Loans> loan = loansRepository.findById(loanId);
        LoansDto loansDto = loansMapper.toDto(loan.get());
//        AssetDTO assetDTO = apiClient.getAssetDTO(loansDto.getAssetCode());
        HardwareDto hardwareDto = apiClient.getHardwareDto(loansDto.getAssetCode());
        APIResponseDTO responseDTO = new APIResponseDTO();
        responseDTO.setLoans(loansDto);
        responseDTO.setHardware(hardwareDto);
        return responseDTO;
    }

    @Override
    public Page<LoansDto> getApproveLoansList(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);

        Page<Loans> loansPage = loansRepository.findAll(pageable);

        List<LoansDto> loanDTOs = loansPage.getContent().stream()
                .filter(loans -> loans.getStatus().equals(LoanStatus.APPROVED))
                .map(loans -> loansMapper.toDto(loans))
                .toList();
        return new PageImpl<>(loanDTOs, pageable, loansPage.getTotalElements());
    }

    @Override
    public Page<LoansDto> getReturnedLoansList(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);

        Page<Loans> loansPage = loansRepository.findAll(pageable);

        List<LoansDto> loanDTOs = loansPage.getContent().stream()
                .filter(loans -> loans.getStatus().equals(LoanStatus.RETURNED))
                .map(loans -> loansMapper.toDto(loans))
                .toList();
        return new PageImpl<>(loanDTOs, pageable, loansPage.getTotalElements());
    }

    @Override
    public String rejectOrder(Long id) {
        Loans loans = loansRepository.findById(id).get();
        loans.setStatus(LoanStatus.REJECTED);
        loansRepository.save(loans);
        return "Order rejected";
    }




    /*
    check trước khi tạo đơn nếu số lượng hardware = 0 thì cảnh báo, một người không thể
    mượn quá 2 hardware
     */

//    public String checkLoan(LoansDto loansDto, Principal principal) {
//        HardwareDto hardwareDto = apiClient.getHardwareDto(loansDto.getAssetCode());
//        if (hardwareDto.getQuantity() == 0) {
//            return "The product is out of stock";
//        } else {
//
//        }
//
//    }
//
//    private Boolean createAnOrder(LoansDto loansDto) {
//        Loans loans = loansMapper.toEntity(loansDto);
//        loansMapper.toDto(loansRepository.save(loans));
//        return true;
//    }
}
