package com.example.managementservice.controller;

import com.example.managementservice.dto.HardwareDto;
import com.example.managementservice.service.HardwareService;
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
        name = "Management Service - Hardware Controller"
)
@RestController
@RequestMapping("api/v1/hardware")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HardwareController {
    HardwareService hardwareService;

    @Operation(
            summary = "This method is a RESTful API endpoint that returns a single hardware resource based on the provided ID",
            description = "@param hardwareId The ID of the hardware resource to retrieve \n" +
                    "@return a object with the retrieved hardware resource, or an error message if the request fails"
    )
    @GetMapping("{id}")
    public ResponseEntity<HardwareDto> getHardwareById(@PathVariable("id") Long hardwareId) {
        HardwareDto hardwareDTO = hardwareService.getHardwareById(hardwareId);
        return new ResponseEntity<>(hardwareDTO, HttpStatus.OK);
    }
    @Operation(
            summary = "This method is a RESTful API endpoint that creates a new hardware resource",
            description = "@param hardware The hardware resource to create \n" +
                    "@return A {@code ResponseEntity} object with a status code indicating whether the request was successful"
    )
    @PostMapping("/create")
    public ResponseEntity<String> createHardware(@RequestPart @Valid HardwareDto hardware, @RequestPart List<MultipartFile> files) {
        if (hardwareService.insertHardware(hardware, files)) {
            return new ResponseEntity<>("Created successfully!",HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Not create successfully!",HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/createList")
    public ResponseEntity<List<HardwareDto>> createList(@RequestBody List<HardwareDto> hardwares) {
        if (hardwareService.insertHardwareList(hardwares)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
            summary = "This method is a RESTful API endpoint that returns a list of all hardware resources.",
            description = """
                    @param page The page number of the results to return\s
                    @param size The number of results to return per page\s
                    @return A page of hardware resources"""
    )
    @GetMapping("/list")
    public Page<HardwareDto> getAllHardware(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "4") int size, @RequestParam(name = "searchText", defaultValue = "") String searchText) {
        return hardwareService.getAllPaginated(page, size, searchText);
    }

    @Operation(
            summary = "This method is a RESTful API endpoint that deletes a hardware resource based on the provided ID.",
            description = "@param id The ID of the hardware resource to delete \n" +
                    "@return A message indicating whether the delete was successful"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteHardwareById(@PathVariable Long id) {
        String message = hardwareService.deleteHardwareById(id);
        if (message.contains("Successfully")) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @GetMapping("/manufactuer/{name}")
    public ResponseEntity<List<HardwareDto>> getByManufactuer(@PathVariable("name") String name) {
        List<HardwareDto> list = hardwareService.getHardwareByManufacturer(name);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateHardware(@PathVariable Long id, @RequestBody @Valid HardwareDto hardwareDTO) throws Exception {
        String message = hardwareService.updateHardware(id, hardwareDTO);
        if (message.contains("Successfully")) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @GetMapping("/asset/{assetCode}")
    public ResponseEntity<HardwareDto> getByAssetCode(@PathVariable("assetCode") String assetCode) {
        HardwareDto hardwareDTO = hardwareService.getHardwareByAssetCode(assetCode);
        return new ResponseEntity<>(hardwareDTO, HttpStatus.OK);
    }
}
