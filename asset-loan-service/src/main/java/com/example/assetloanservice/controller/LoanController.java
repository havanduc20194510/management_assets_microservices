package com.example.assetloanservice.controller;

import com.example.assetloanservice.dto.APIResponseDTO;
import com.example.assetloanservice.dto.LoanDetailResponseDto;
import com.example.assetloanservice.dto.LoanResponseDto;
import com.example.assetloanservice.dto.LoansDto;
import com.example.assetloanservice.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api/v1/loan")
public class LoanController {
    private LoanService loanService;

    @Operation(
            summary = "API để lấy thông tin chi tiết về đơn mượn",
            description = "API để lấy thông tin chi tiết về một đơn mượn, gồm thời gian, số lượng, người mượn"
    )
    @GetMapping("/{id}")
    public ResponseEntity<APIResponseDTO> getDetails(@PathVariable("id") Long id) {
        APIResponseDTO responseDTO = loanService.getInfo(id);
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

    @GetMapping("removeOrder/{id}")
    public ResponseEntity<String> removeOrder(@PathVariable("id") Long id) {
        String message = loanService.rejectOrder(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(
            summary = "Creates a new loan order",
            description = "Creates a new loan order with the given details. " +
                    "If the loan order is valid, the loan will be created and the " +
                    "client will be notified. If the loan order is invalid, an error " +
                    "will be returned."
    )
    @PostMapping("/create")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<LoanResponseDto> createAnOrder(@RequestBody LoansDto order) {
        LoanResponseDto response = loanService.createAnOrder(order);
        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/list")
    public Page<LoanDetailResponseDto> getAllLoansListByUserName(@RequestParam("username") String username, @RequestParam(name = "page", defaultValue = "2") int page, @RequestParam(name = "size", defaultValue = "2") int size) {
        return loanService.getAllLoansListByUserName(username, page, size);
    }
}
