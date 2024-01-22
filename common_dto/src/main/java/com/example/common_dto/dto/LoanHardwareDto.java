package com.example.common_dto.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoanHardwareDto {
    private String assetCode;
    private int quantity;
}
