package com.example.assetloanservice.controller;

import com.example.assetloanservice.dto.APIResponseDTO;
import com.example.assetloanservice.dto.LoansDto;
import com.example.assetloanservice.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Loan Service - LoanController",
        description = "Restful service for loan "
)
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/loan")
public class LoanController {
    private LoanService loanService;

    @Operation(
            summary = "API để lấy thông tin chi tiết về đơn mượn",
            description = "API để lấy chi tiết về một sản phẩm có trong đơn mượn thông qua AssetCode" +
                    "sẽ tiếp tục phát triển thêm để lấy thông tin về người mượn khi phát triển đến user service"
    )
    @GetMapping("/{id}")
    public ResponseEntity<APIResponseDTO> getDetails(@PathVariable("id") Long id) {
        APIResponseDTO responseDTO = loanService.getLoanDetails(id);
        if (responseDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Returns an page of loans"
    )
    @GetMapping("/approve-list")
    public Page<LoansDto> getApprove(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "2") int size) {
        return loanService.getApproveLoansList(page, size);
    }

    @GetMapping("/returned-list")
    public Page<LoansDto> getReturned(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "2") int size) {
        return loanService.getReturnedLoansList(page, size);
    }

}
