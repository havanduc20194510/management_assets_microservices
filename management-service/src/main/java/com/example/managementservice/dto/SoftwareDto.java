package com.example.managementservice.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.example.managementservice.entity.Software}
 */
@Value
public class SoftwareDto implements Serializable {
    Long id;
    String name;
    String assetCode;
    String version;
    String licenseKey;
    Integer seats;
    Double pricePerSeat;
    String vendor;
    String description;
    Date createDate;
    Date updateDate;
}