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
import com.example.common_dto.dto.EventStatus;
import com.example.common_dto.dto.LoanDetailsDto;
import com.example.common_dto.dto.LoanHardwareDto;
import com.example.common_dto.event.LoanApprovedEvent;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {
    private LoanDetailsRepository loanDetailsRepository;
    private LoansRepository loansRepository;
    private LoansMapper loansMapper;
    private APIClient apiClient;
    private final NewTopic topic;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanServiceImpl.class);
    private final KafkaTemplate<String, LoanApprovedEvent> kafkaTemplate;


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

    /*
    set trạng thái, lưu lại đơn mượn kèm lý do từ chối vào db, cập nhật ngày từ chối
     */
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

    /* approve Order
    set trạng thái đơn là Approve,
    trừ số lượng sản phẩm trong kho
    đã có API để check số lượng lấy API đó để làm điều kiện duyệt
    lưu lại tên người duyệt
     */
    // mô hình thiết kế SAGA Choreography để xử lý một transaction

    // admin side only

    /*
    user side only
    người dùng tạo đơn mượn, người dùng có thể xem đơn mượn của mình, đơn nào được duyệt và đơn nào bị từ chối
    tạo một đơn mượn
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
    @Transactional
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

    // cr đã xong còn UPDATE và DELETE
    /*
    workflow của một chức năng phê duyệt đơn mượn:
    - admin phê duyệt đơn mượn
    - hệ thống sẽ kiếm tra các thông tin có trên đơn mượn,
    ví dụ: số lượng sản phẩm có trong đơn
    - nếu không có lỗi, đơn mượn sẽ được set trạng thái duyệt (APPROVED) và lưu vào database
    nếu hệ thống kiếm tra có bất kỳ thông tin nào không hợp lệ,
    sẽ hiển thị ra lỗi cho admin, và admin sẽ tạo một thông báo gửi về cho người mượn
     */
    @Transactional
    @Override
    public LoansDto ApproveLoanOrder(Long loanId) {
        String transactionId = UUID.randomUUID().toString();

        Loans loans;
        try {
            loans = loansRepository.findById(loanId).get();
            // PUBLISH event lên kafka topic để MS xử lý kho
            LoanApprovedEvent event = new LoanApprovedEvent();
            event.setDetails(getLoanDetailsDto(loans.getDetails()));
            event.setStatus(EventStatus.PENDING_INVENTORY);
            LOGGER.info(String.format("ApproveLoanOrder => %s", event));
            Message<LoanApprovedEvent> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, topic.name())
                    .setHeader("transaction_id", transactionId)
                    .build();
            kafkaTemplate.send(message);

        } catch (Exception e) {
            LOGGER.error("Error publishing event, transaction_id=" + transactionId, e);
            throw e;
        }
        return loansMapper.toDto(loans);
    }
    
    public LoanDetailsDto getLoanDetailsDto(List<LoanDetails> loanDetailsList) {
        LoanDetailsDto detailsDto = new LoanDetailsDto();

        if (loanDetailsList.size() == 1) {
            LoanDetails detail = loanDetailsList.get(0);
            LoanHardwareDto hardwareDto = new LoanHardwareDto();
            hardwareDto.setAssetCode(detail.getAssetCode());
            hardwareDto.setQuantity(detail.getQuantity());

            List<LoanHardwareDto> hardwareDtos = new ArrayList<>();
            hardwareDtos.add(hardwareDto);

            detailsDto.setProducts(hardwareDtos);
        } else {
            List<LoanHardwareDto> hardwareDtos = new ArrayList<>();
            for(LoanDetails detail : loanDetailsList) {
                LoanHardwareDto hardwareDto = new LoanHardwareDto();
                hardwareDto.setAssetCode(detail.getAssetCode());
                hardwareDto.setQuantity(detail.getQuantity());

                hardwareDtos.add(hardwareDto);
            }
            detailsDto.setProducts(hardwareDtos);
        }
        return detailsDto;
    }


}