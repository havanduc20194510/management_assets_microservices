package com.example.managementservice.service;

import com.example.managementservice.dto.CartDto;

public interface CartService {
    CartDto addToCart(Long userId, Long productId, int quantity);
    CartDto getCart(Long userId, boolean checkout);

    boolean checkout(Long userId);

    boolean updateQuantity(Long userId, Long hardwareId, int quantity);

    boolean deleteHardwareFromCart(Long userId, Long hardwareId);

}
