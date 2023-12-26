package com.example.assetloanservice.dto;

import lombok.Value;

import java.io.Serializable;
import java.sql.Date;
@Value
public class HardwareDto implements Serializable {
    Long id;
    String name;
    int quantity;
    String manufacturer;
    String description;
    String assetCode;
    String serialNumber;
    String type;
    String location;
    Long purchaseValue;
    Date purchaseDate;
}
