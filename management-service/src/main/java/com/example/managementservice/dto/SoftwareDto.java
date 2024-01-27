package com.example.managementservice.dto;

import com.example.managementservice.entity.SoftwareImage;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link com.example.managementservice.entity.Software}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    List<SoftwareImage> images;
}
