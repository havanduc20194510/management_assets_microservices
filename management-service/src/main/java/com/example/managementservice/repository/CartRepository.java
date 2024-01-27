package com.example.managementservice.repository;

import com.example.managementservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserIdAndCheckout(Long userId, boolean checkout);

}
