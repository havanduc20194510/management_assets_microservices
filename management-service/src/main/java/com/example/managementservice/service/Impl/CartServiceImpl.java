package com.example.managementservice.service.Impl;

import com.example.managementservice.dto.CartDto;
import com.example.managementservice.entity.Cart;
import com.example.managementservice.entity.CartDetail;
import com.example.managementservice.entity.Hardware;
import com.example.managementservice.mapper.CartMapper;
import com.example.managementservice.repository.CartDetailRepository;
import com.example.managementservice.repository.CartRepository;
import com.example.managementservice.repository.HardwareRepository;
import com.example.managementservice.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private CartDetailRepository cartDetailRepository;

    private CartMapper cartMapper;
    private final HardwareRepository hardwareRepository;

    @Override
    public CartDto addToCart(Long userId, Long hardwareId, int quantity) {
        Cart currentCart = cartRepository.findByUserIdAndCheckout(userId, false);
        if (currentCart == null) {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setCheckout(false);
            cartRepository.save(cart);
            CartDetail cartDetail = new CartDetail();
            cartDetail.setCartId(cart.getId());
            cartDetail.setHardwareId(hardwareId);
            cartDetail.setQuantity(quantity);
            cartDetailRepository.save(cartDetail);
        } else {
            CartDetail cartDetail = cartDetailRepository.findByCartIdAndHardwareId(currentCart.getId(), hardwareId);
            if (cartDetail != null) {
                cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
                cartDetailRepository.save(cartDetail);
            } else {
                CartDetail newCartDetail = new CartDetail();
                newCartDetail.setCartId(currentCart.getId());
                newCartDetail.setHardwareId(hardwareId);
                newCartDetail.setQuantity(quantity);
                cartDetailRepository.save(newCartDetail);
            }
        }
         Cart cart = cartRepository.findByUserIdAndCheckout(userId, false);
         return cartMapper.toDto(cart);
    }

    @Override
    public CartDto getCart(Long userId, boolean checkout) {
        Cart cart = cartRepository.findByUserIdAndCheckout(userId, checkout);
        List<CartDetail> cartDetails = cartDetailRepository.findByCartId(cart.getId());
        CartDto cartDto = cartMapper.toDto(cart);
        List<Hardware> hardwareList = new ArrayList<>();
        for (CartDetail cartDetail : cartDetails) {
            Hardware hardware = hardwareRepository.findById(cartDetail.getHardwareId()).get();
            hardware.setQuantity(cartDetail.getQuantity());
            hardwareList.add(hardware);
        }
        cartDto.setCartDetails(hardwareList);
        return cartDto;
    }

    @Override
    public boolean checkout(Long userId) {
        Cart cart = cartRepository.findByUserIdAndCheckout(userId, false);
        if (cart != null) {
            cart.setCheckout(true);
            cartRepository.save(cart);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateQuantity(Long userId, Long hardwareId, int quantity) {
        Cart cart = cartRepository.findByUserIdAndCheckout(userId, false);
        if (cart != null) {
            CartDetail cartDetail = cartDetailRepository.findByCartIdAndHardwareId(cart.getId(), hardwareId);
            if (cartDetail != null) {
                cartDetail.setQuantity(quantity);
                cartDetailRepository.save(cartDetail);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteHardwareFromCart(Long userId, Long hardwareId) {
        Cart cart = cartRepository.findByUserIdAndCheckout(userId, false);
        if (cart != null) {
            CartDetail cartDetail = cartDetailRepository.findByCartIdAndHardwareId(cart.getId(), hardwareId);
            if (cartDetail != null) {
                cartDetailRepository.delete(cartDetail);
                return true;
            }
        }
        return false;
    }
}
