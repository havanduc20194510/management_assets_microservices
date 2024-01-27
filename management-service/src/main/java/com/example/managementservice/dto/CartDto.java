package com.example.managementservice.dto;
import com.example.managementservice.entity.Hardware;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long id;
    private Long userId;
    private boolean Checkout;
    private List<Hardware> cartDetails;
}
