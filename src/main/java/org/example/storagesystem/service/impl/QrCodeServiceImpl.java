package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.qrCode.QrCodeDto;
import org.example.storagesystem.dto.qrCode.QrCodeRequest;
import org.example.storagesystem.entity.Cell;
import org.example.storagesystem.entity.QRCode;
import org.example.storagesystem.entity.Storage;
import org.example.storagesystem.entity.StorageObject;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;
import org.example.storagesystem.mapper.QrCodeMapper;
import org.example.storagesystem.repository.CellRepository;
import org.example.storagesystem.repository.QrCodeRepository;
import org.example.storagesystem.repository.StorageObjectRepository;
import org.example.storagesystem.repository.StorageRepository;
import org.example.storagesystem.service.QrCodeService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {
    private final QrCodeMapper qrCodeMapper;
    private final CellRepository cellRepository;
    private final QrCodeRepository qrCodeRepository;
    private final StorageRepository storageRepository;
    private final StorageObjectRepository storageObjectRepository;

    @Override
    public QrCodeDto createCodeForStorage(QrCodeRequest qrCodeRequest, Long storageId) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, storageId));

        QRCode qrCode;

        if (qrCodeRepository.existsByStorageId(storageId)) {
            qrCode = qrCodeRepository.findByStorageId(storageId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.QRCODE_NOT_FOUND));
        } else {
            qrCode = new QRCode();
        }

        qrCode.setStorage(storage);
        qrCode.setQrContent(qrCodeRequest.getQrContent());

        qrCodeRepository.save(qrCode);

        return qrCodeMapper.mapToForStorage(qrCode);
    }

    @Override
    public QrCodeDto createCodeForCell(QrCodeRequest qrCodeRequest, Long cellId) {
        Cell cell = cellRepository.findById(cellId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND));

        QRCode qrCode;

        if (qrCodeRepository.existsByCellId(cellId)) {
            qrCode = qrCodeRepository.findByCellId(cellId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.QRCODE_NOT_FOUND));
        } else {
            qrCode = new QRCode();
        }

        qrCode.setCell(cell);
        qrCode.setQrContent(qrCodeRequest.getQrContent());

        qrCodeRepository.save(qrCode);

        return qrCodeMapper.mapToForCell(qrCode);
    }

    @Override
    public QrCodeDto createCodeForObject(QrCodeRequest qrCodeRequest, Long objectId) {
       StorageObject storageObject = storageObjectRepository.findById(objectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_OBJECT_NOT_FOUND, objectId));

        QRCode qrCode;

        if (qrCodeRepository.existsByStorageObjectId(objectId)) {
            qrCode = qrCodeRepository.findByStorageObjectId(objectId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.QRCODE_NOT_FOUND));
        } else {
            qrCode = new QRCode();
        }

        qrCode.setStorageObject(storageObject);
        qrCode.setQrContent(qrCodeRequest.getQrContent());

        qrCodeRepository.save(qrCode);

        return qrCodeMapper.mapToForStorageObject(qrCode);
    }

    @Override
    public QrCodeDto findQrCodeByStorage(Long storageId) {
        QRCode qrCode = qrCodeRepository.findByStorageId(storageId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QRCODE_NOT_FOUND));

        return qrCodeMapper.mapToForStorage(qrCode);
    }

    @Override
    public QrCodeDto findQrCodeByCell(Long cellId) {
        QRCode qrCode = qrCodeRepository.findByCellId(cellId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QRCODE_NOT_FOUND));

        return qrCodeMapper.mapToForCell(qrCode);
    }

    @Override
    public QrCodeDto findQrCodeByStorageObject(Long objectId) {
        QRCode qrCode = qrCodeRepository.findByStorageObjectId(objectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QRCODE_NOT_FOUND));

        return qrCodeMapper.mapToForStorageObject(qrCode);
    }
}
