package com.example.managementservice.controller;

import com.example.managementservice.dto.SoftwareDto;
import com.example.managementservice.service.SoftwareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Tag(
        name = "Management Service - Software Controller"
)
@RestController
@RequestMapping("/api/v1/software")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SoftwareController {
    private SoftwareService softwareService;

    @Operation(
            summary = "creates a new Software record",
            description = "@param softwareDto the software data to be created" +
                    "@return the created Software data"
    )
    @PostMapping("/create")
    ResponseEntity<SoftwareDto> insertSoftware(@RequestPart @Valid SoftwareDto softwareDto, @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        SoftwareDto savedSoftwareDto = softwareService.insertSoftware(softwareDto, images);
        return new ResponseEntity<>(savedSoftwareDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Retrieves a software record by its ID",
            description = "@param softwareId the ID of the software record to retrieve" +
                    "@return the retrieved software data"
    )
    @GetMapping("/{id}")
    ResponseEntity<SoftwareDto> getSoftwareById(@PathVariable("id") Long softwareId) {
        SoftwareDto softwareDto = softwareService.getSoftwareById(softwareId);
        if (softwareDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(softwareDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an existing software record",
            description = "@param softwareId the ID of the software record to update" +
                    "@param softwareDto the updated software data" +
                    "@return the updated software data"
    )
    @PutMapping("/update/{id}")
    ResponseEntity<SoftwareDto> updateSoftware(@PathVariable("id") Long softwareId, @RequestBody SoftwareDto softwareDto) {
        SoftwareDto updatedSoftwareDto = softwareService.updateSoftware(softwareId, softwareDto);
        if (updatedSoftwareDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedSoftwareDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes an existing software record",
            description = "@param softwareId the ID of the software record to delete" +
                    "@return a message indicating the result of the deletion"
    )
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteSoftware(@PathVariable("id") Long softwareId) {
        String message = softwareService.deleteSoftwareById(softwareId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SoftwareDto>> findSoftwareByName(@RequestParam String name) {
        List<SoftwareDto> softwareDtos = softwareService.findByName(name);
        return new ResponseEntity<>(softwareDtos, HttpStatus.OK);
    }

    @GetMapping("/search/vendor")
    public ResponseEntity<List<SoftwareDto>> findSoftwareByVendor(@RequestParam String vendor) {
        List<SoftwareDto> softwareDtos = softwareService.findByVendor(vendor);
        return new ResponseEntity<>(softwareDtos, HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieves a page of software records",
            description = "@param page the page number" +
                    "@param size the page size" +
                    "@return a page of software data"
    )
    @GetMapping("/list")
    public Page<SoftwareDto> getAllSoftware(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "2") int size) {
        return softwareService.getAllPaginated(page, size);
    }

    @GetMapping("/asset/{assetCode}")
    public ResponseEntity<SoftwareDto> getByAssetCode(@PathVariable("assetCode") String assetCode) {
        SoftwareDto softwareDto = softwareService.getSoftwareByAssetCode(assetCode);
        return new ResponseEntity<>(softwareDto, HttpStatus.OK);
    }
}
