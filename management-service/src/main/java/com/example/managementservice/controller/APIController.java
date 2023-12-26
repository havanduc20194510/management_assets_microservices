package com.example.managementservice.controller;

import com.example.managementservice.dto.AssetDTO;
import com.example.managementservice.service.Impl.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(
        name = "Management Service - APIController",
        description = "This controller is used to create an API endpoint that retrieves product information and " +
                "returns an AssetDto to be used to communicate between services"
)
@RestController
@RequestMapping("api/products")
@AllArgsConstructor
public class APIController {
    private ProductService productService;

    @Operation(
            summary = "This method is used to retrieve a product based on the asset code.",
            description = "A product consists only of general information of hardware and software \n" +
                    "@param assetCode the asset code of the product" +
                    "@return the AssetDTO if found, otherwise a 404 Not Found response"


    )
    @GetMapping("/{assetCode}")
    public ResponseEntity<AssetDTO> getProductByAssetCode(@PathVariable("assetCode") String assetCode) {
        AssetDTO productDTO = productService.getProductByAssetCode(assetCode);
        if (productDTO != null) {
            return ResponseEntity.ok(productDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
