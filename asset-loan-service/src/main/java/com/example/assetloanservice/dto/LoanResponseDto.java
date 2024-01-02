package com.example.assetloanservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanResponseDto {
    public String message;
    public boolean success;
}
