package com.example.managementservice.repository;

import com.example.managementservice.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    CartDetail findByCartIdAndHardwareId(Long cartId, Long hardwareId);

    List<CartDetail> findByCartId(Long id);
}
