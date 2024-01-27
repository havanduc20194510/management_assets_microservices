package com.example.managementservice.dto;

import com.example.managementservice.entity.HardwareImage;
import lombok.Value;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Value
public class HardwareDto implements Serializable {
    Long id;
    String name;
    int quantity;
    String manufacturer;
    String description;
    String assetCode;
    String serialNumber;
    String location;
    Long purchaseValue;
    Date purchaseDate;
    List<HardwareImage> images;
}
