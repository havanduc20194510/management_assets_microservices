package com.example.managementservice.controller;

import com.example.managementservice.dto.CartDto;
import com.example.managementservice.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Management Service - Cart Controller"
)
@RestController
@RequestMapping("api/v1/hardware/cart")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CartController {
    private CartService cartService;
    @PostMapping("/create")
    public ResponseEntity<CartDto> createCart(@RequestParam("userId") Long userId, @RequestParam("hardwareId") Long hardwareId, @RequestParam(value = "quantity", defaultValue = "1") int quantity) {
        CartDto cartDto = cartService.addToCart(userId, hardwareId, quantity);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<CartDto> getCart(@RequestParam("userId") Long userId, @RequestParam("checkout") boolean checkout) {
        CartDto cartDto = cartService.getCart(userId, checkout);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PutMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestParam("userId") Long userId) {
        if (cartService.checkout(userId)) {
            return new ResponseEntity<>("Checkout successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Checkout failed!", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update-quantity")
    public ResponseEntity<String> updateQuantity(@RequestParam("userId") Long userId, @RequestParam("hardwareId") Long hardwareId, @RequestParam("quantity") int quantity) {
        if (cartService.updateQuantity(userId, hardwareId, quantity)) {
            return new ResponseEntity<>("Update successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Update failed!", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteHardwareFromCart(@RequestParam("userId") Long userId, @RequestParam("hardwareId") Long hardwareId) {
        if (cartService.deleteHardwareFromCart(userId, hardwareId)) {
            return new ResponseEntity<>("Delete successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Delete failed!", HttpStatus.BAD_REQUEST);
    }
}
