package org.example.storagesystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.storage.StorageDto;
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
@RequestMapping("dev-api.storage-system.ru/v1/storages")
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

    @GetMapping("/{id}")
    public ResponseEntity<StorageDto> findById(@PathVariable(value = "id") Long storageId) {
        return new ResponseEntity<>(storageService.findWithChildrenById(storageId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<StorageDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
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
