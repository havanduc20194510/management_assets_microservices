package com.example.assetloanservice.service.Impl;

import com.example.assetloanservice.Enum.LoanStatus;
import com.example.assetloanservice.dto.APIResponseDTO;
import com.example.assetloanservice.dto.LoanResponseDto;
import com.example.assetloanservice.dto.LoansDto;
import com.example.assetloanservice.entity.LoanDetails;
import com.example.assetloanservice.entity.Loans;
import com.example.assetloanservice.mapper.LoansMapper;
import com.example.assetloanservice.repository.LoanDetailsRepository;
import com.example.assetloanservice.repository.LoansRepository;
import com.example.assetloanservice.service.APIClient;
import com.example.assetloanservice.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {
    private LoanDetailsRepository loanDetailsRepository;
    private LoansRepository loansRepository;
    private LoansMapper loansMapper;
    private APIClient apiClient;


    @Override
    public APIResponseDTO getInfo(Long loansId) {
        Loans loans = loansRepository.findById(loansId).get();
        List<LoanDetails> details = loanDetailsRepository.findByLoansId(loans.getId());
        loans.setDetails(details);
        LoansDto dto = loansMapper.toDto(loans);
        APIResponseDTO responseDTO = new APIResponseDTO();
        responseDTO.setLoans(dto);
        return responseDTO;
    }

    @Override
    public Page<LoansDto> getApproveLoansList(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);

        Page<Loans> loansPage = loansRepository.findAll(pageable);

        List<LoansDto> loanDTOs = loansPage.getContent().stream()
                .filter(loans -> loans.getStatus() != null &&
                        loans.getStatus().equals(LoanStatus.APPROVED))
                .map(loans -> loansMapper.toDto(loans))
                .toList();
        return new PageImpl<>(loanDTOs, pageable, loansPage.getTotalElements());
    }

    @Override
    public Page<LoansDto> getReturnedLoansList(int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);

        Page<Loans> loansPage = loansRepository.findAll(pageable);

        List<LoansDto> loanDTOs = loansPage.getContent().stream()
                .filter(loans -> loans.getStatus() != null &&
                        loans.getStatus().equals(LoanStatus.RETURNED))
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

    @Override
    public String rejectAll() {
        loansRepository.findAll().stream()
                .filter(loans -> loans.getStatus().equals(LoanStatus.PENDING))
                .forEach(loan -> {
                    loan.setStatus(LoanStatus.REJECTED);
                    loansRepository.save(loan);
                });
        return "REJECTED ALL";
    }
    // admin side only

    /*
    user side only
    người dùng tạo đơn mượn, người dùng có thể xem đơn mượn của mình, đơn nào được duyệt và đơn nào bị từ chối
    tạo một đơn mượn
     */

    /* approve Order
    kiểm tra số lượng sản phẩm
    xác thực người dùng : có thể dùng Principal
    đạt đủ điều kiện thì phê duyệt
     */

    // method lấy ra sp kèm số lượng trong đơn
    Map<String, Integer> getLoanQuantity(List<LoanDetails> detailsList) {
        Map<String, Integer> loanQuantityMap = new HashMap<>();
        for (LoanDetails details : detailsList) {
            String assetCode = details.getAssetCode();
            int quantity = details.getQuantity();

            if (!loanQuantityMap.containsKey(assetCode)) {
                loanQuantityMap.put(assetCode, quantity);
            } else {
                int currentQuantity = loanQuantityMap.get(assetCode);
                loanQuantityMap.put(assetCode, currentQuantity + quantity);
            }
        }
        return loanQuantityMap;
    }
    @Override
    public LoanResponseDto createAnOrder(LoansDto order) {
        LoanResponseDto dto = new LoanResponseDto();
        try {
            Loans loan = loansMapper.toEntity(order);
            // Chú ý: Bạn cần tạo danh sách LoanDetails sau khi đã có đối tượng Loans
            loan.setDetails(order.getDetails());
            loan.setStatus(LoanStatus.PENDING);
            for (LoanDetails detail : loan.getDetails()) {
                detail.setLoans(loan); // Thiết lập quan hệ với Loans
            }
            String result = apiClient.validateQuantity(getLoanQuantity(loan.getDetails()));

            if (result == null) {
                loansRepository.save(loan);
                dto.setMessage("Thành công");
                dto.setSuccess(true);
            } else {
                dto.setMessage(result);
                dto.setSuccess(false);
            }
        } catch (Exception e) {
            // Ghi log lỗi ở đây nếu cần
            dto.setMessage(e.getMessage());
            dto.setSuccess(false);
        }
        return dto;
    }

}