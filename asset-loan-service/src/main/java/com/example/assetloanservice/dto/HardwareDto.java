package com.example.assetloanservice.dto;

import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    List<Image> images;
}
