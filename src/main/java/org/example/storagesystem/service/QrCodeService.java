package org.example.storagesystem.service;

import org.example.storagesystem.dto.qrCode.QrCodeDto;
import org.example.storagesystem.dto.qrCode.QrCodeRequest;

public interface QrCodeService {
    QrCodeDto createCodeForStorage(QrCodeRequest qrCodeRequest, Long storageId);

    QrCodeDto createCodeForCell(QrCodeRequest qrCodeRequest, Long cellId);

    QrCodeDto createCodeForObject(QrCodeRequest qrCodeRequest, Long objectId);

    QrCodeDto findQrCodeByStorage(Long storageId);

    QrCodeDto findQrCodeByCell(Long cellId);

    QrCodeDto findQrCodeByStorageObject(Long objectId);
}
