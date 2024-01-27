package com.example.managementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssetDTO {
    private Long id;
    private String assetName;
    private String assetDescription;
    private String assetCode;
}
