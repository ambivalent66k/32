package org.example.storagesystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.qrCode.QrCodeDto;
import org.example.storagesystem.dto.qrCode.QrCodeRequest;
import org.example.storagesystem.service.QrCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/")
public class QrCodeController {
    private final QrCodeService qrCodeService;

    @PostMapping("/storages/{storage_id}/qr-code")
    public ResponseEntity<QrCodeDto> createCodeForStorage(@RequestBody QrCodeRequest qrCodeRequest,
                                                          @PathVariable("storage_id") Long storageId) {
        return new ResponseEntity<>(qrCodeService.createCodeForStorage(qrCodeRequest, storageId), HttpStatus.CREATED);
    }

    @GetMapping("/storages/{storage_id}/qr-code")
    public ResponseEntity<QrCodeDto> getCodeForStorage(@PathVariable("storage_id") Long storageId) {
        return new ResponseEntity<>(qrCodeService.findQrCodeByStorage(storageId), HttpStatus.OK);
    }

    @PostMapping("/cells/{cell_id}/qr-code")
    public ResponseEntity<QrCodeDto> createCodeForCell(@RequestBody QrCodeRequest qrCodeRequest,
                                                       @PathVariable("cell_id") Long cellId) {
        return new ResponseEntity<>(qrCodeService.createCodeForCell(qrCodeRequest, cellId), HttpStatus.CREATED);
    }

    @GetMapping("/cells/{cell_id}/qr-code")
    public ResponseEntity<QrCodeDto> getCodeForCell(@PathVariable("cell_id") Long cellId) {
        return new ResponseEntity<>(qrCodeService.findQrCodeByCell(cellId), HttpStatus.OK);
    }

    @PostMapping("/objects/{object_id}/qr-code")
    public ResponseEntity<QrCodeDto> createCodeForObject(@RequestBody QrCodeRequest qrCodeRequest,
                                                         @PathVariable("object_id") Long objectId) {
        return new ResponseEntity<>(qrCodeService.createCodeForObject(qrCodeRequest, objectId), HttpStatus.CREATED);
    }

    @GetMapping("/objects/{object_id}/qr-code")
    public ResponseEntity<QrCodeDto> getCodeForObject(@PathVariable("object_id") Long objectId) {
        return new ResponseEntity<>(qrCodeService.findQrCodeByStorageObject(objectId), HttpStatus.OK);
    }
}
