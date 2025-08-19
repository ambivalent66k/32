package org.example.storagesystem.mapper;

import org.example.storagesystem.dto.qrCode.QrCodeDto;
import org.example.storagesystem.dto.qrCode.QrCodeRequest;
import org.example.storagesystem.entity.QRCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QrCodeMapper {
    @Mapping(target = "storageId", source = "storage.id")
    QrCodeDto mapToForStorage(QRCode qrCode);

    @Mapping(target = "cellId", source = "cell.id")
    QrCodeDto mapToForCell(QRCode qrCode);

    @Mapping(target = "storageObjectId", source = "storageObject.id")
    QrCodeDto mapToForStorageObject(QRCode qrCode);

    QRCode mapTo(QrCodeRequest qrCode);
}
