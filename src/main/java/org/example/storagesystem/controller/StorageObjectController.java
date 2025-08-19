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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("http://10.10.146.221/api/v1/objects")
public class StorageObjectController {
    private final StorageObjectService storageObjectService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StorageObjectDto> create(@Valid @RequestPart("object") StorageObjectDto StorageObjectDto,
                                                   @RequestPart(value = "photo", required = false) MultipartFile photo,
                                                   BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(storageObjectService.createObject(StorageObjectDto, photo), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StorageObjectDto> update(@Valid @RequestPart("object") StorageObjectPatchDto storageObjectPatchDto,
                                                   @PathVariable(value = "id") Long objectId,
                                                   @RequestPart(value = "photo", required = false) MultipartFile photo,
                                             BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(
                storageObjectService.updateObject(storageObjectPatchDto, photo,objectId),
                HttpStatus.OK
        );
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
            @RequestParam(defaultValue = "10") int size) {
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
