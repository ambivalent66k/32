package org.example.storagesystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.storageObject.StorageObjectChangeQuantityDto;
import org.example.storagesystem.dto.storageObject.StorageObjectDto;
import org.example.storagesystem.dto.storageObject.StorageObjectMove;
import org.example.storagesystem.dto.storageObject.StorageObjectPatchDto;
import org.example.storagesystem.dto.storageObject.response.StorageObjectDtoResponse;
import org.example.storagesystem.service.StorageObjectService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev-api.storage-system.ru/v1/storage-objects")
public class StorageObjectController {
    private final StorageObjectService storageObjectService;

    @PostMapping()
    public ResponseEntity<StorageObjectDto> create(@Valid @RequestBody() StorageObjectDto StorageObjectDto,
                                                   BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(storageObjectService.createObject(StorageObjectDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StorageObjectDto> update(@Valid @RequestBody StorageObjectPatchDto storageObjectPatchDto,
                                             @PathVariable(value = "id") Long objectId,
                                             BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(storageObjectService.updateObject(storageObjectPatchDto, objectId), HttpStatus.OK);
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<StorageObjectDto> move(@RequestBody StorageObjectMove storageObjectMove,
                                                   @PathVariable(value = "id") Long objectId) {
        return new ResponseEntity<>(storageObjectService.moveObject(storageObjectMove, objectId), HttpStatus.OK);
    }

    @PostMapping("/{id}/quantity")
    public ResponseEntity<StorageObjectDto> move(@RequestBody StorageObjectChangeQuantityDto changeQuantityDto,
                                                   @PathVariable(value = "id") Long objectId) {
        return new ResponseEntity<>(storageObjectService.changeQuantity(changeQuantityDto, objectId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StorageObjectDtoResponse> findById(@PathVariable(value = "id") Long storageId) {
        return new ResponseEntity<>(storageObjectService.findById(storageId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<StorageObjectDtoResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return new ResponseEntity<>(
                storageObjectService.findAll(page, size),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(value = "id") Long storageId) {
        storageObjectService.deleteById(storageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
