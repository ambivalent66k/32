package org.example.storagesystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.storage.StorageDto;
import org.example.storagesystem.dto.storage.StorageMoveDto;
import org.example.storagesystem.dto.storage.response.StorageDtoResponse;
import org.example.storagesystem.dto.storage.response.StorageDtosResponse;
import org.example.storagesystem.dto.storage.StoragePatchDto;
import org.example.storagesystem.service.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("http://10.10.146.221/api/v1/storages")
public class StorageController {
    private final StorageService storageService;

    @PostMapping()
    public ResponseEntity<StorageDto> create(@Valid @RequestBody() StorageDto storageDto,
                                           BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(storageService.createStorage(storageDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StorageDto> update(@Valid @RequestBody StoragePatchDto storagePatchDto,
                                             @PathVariable(value = "id") Long storageId,
                                             BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(storageService.updateStorage(storagePatchDto, storageId), HttpStatus.OK);
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<StorageDtoResponse> move(@RequestBody StorageMoveDto storageMoveDto,
                                             @PathVariable(value = "id") Long storageId){
        return new ResponseEntity<>(storageService.moveStorage(storageMoveDto, storageId), HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<StorageDtoResponse> findById(@PathVariable(value = "id") Long storageId) {
        return new ResponseEntity<>(storageService.findById(storageId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<StorageDtosResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                storageService.findAll(page, size),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(value = "id") Long storageId) {
        storageService.deleteById(storageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
