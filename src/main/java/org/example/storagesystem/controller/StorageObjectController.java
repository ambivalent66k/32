package org.example.storagesystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.storage.StoragePatchDto;
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
@RequestMapping("api/v1/objects")
public class StorageObjectController {
    private final StorageObjectService storageObjectService;

    @Operation(
            summary = "Создание объекта",
            description = "Создаёт новый объект хранилища.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Объект создан",
                            content = @Content(schema = @Schema(implementation = StorageObjectDto.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StorageObjectDto> create(@Valid @Parameter(description = "Данные нового объекта",
                                                               required = true, content = @Content(schema =
                                                       @Schema(implementation = StorageObjectDto.class)))
                                                       @RequestPart("object") StorageObjectDto StorageObjectDto,
                                                   @Parameter(description = "Данные фотографии объекта", required = true,
                                                           content = @Content(schema =
                                                           @Schema(implementation = MultipartFile.class)))
                                                   @RequestPart(value = "photo", required = false) MultipartFile photo,
                                                   BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(storageObjectService.createObject(StorageObjectDto, photo), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновление объекта",
            description = "Изменяет атрибуты объекта.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объект обновлен",
                            content = @Content(schema = @Schema(implementation = StorageObjectDto.class))),
                    @ApiResponse(responseCode = "404", description = "Объект не найден")
            }
    )
    @PatchMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StorageObjectDto> update(@Valid @Parameter(description = "Данные нового объекта",
                                                               required = true, content = @Content(schema =
                                                       @Schema(implementation = StoragePatchDto.class)))
                                                       @RequestPart("object") StorageObjectPatchDto storageObjectPatchDto,
                                                   @Parameter(description = "Данные фотографии объекта", required = true,
                                                           content = @Content(schema =
                                                           @Schema(implementation = MultipartFile.class)))
                                                       @RequestPart(value = "photo", required = false)
                                                   MultipartFile photo,
                                                   @PathVariable(value = "id") Long objectId,
                                             BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(
                storageObjectService.updateObject(storageObjectPatchDto, photo,objectId),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Перемещение объекта",
            description = "Выполняет перемещение объекта в указанное хранилище или ячейку." +
                    "Остальные поля объекта не изменяются.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Хранилище перемещено",
                            content = @Content(schema = @Schema(implementation = StorageObjectMove.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры"),
                    @ApiResponse(responseCode = "404", description = "Не найдено"),
                    @ApiResponse(responseCode = "409", description = "Конфликт доменных ограничений")
            }
    )
    @PostMapping("/{id}/move")
    public ResponseEntity<StorageObjectDto> move(@Parameter(description = "Параметры перемещения объекта",
                                                             required = true,
                                                             content = @Content(schema =
                                                             @Schema(implementation = StorageObjectDto.class)))
            @RequestBody StorageObjectMove storageObjectMove, @Parameter(description = "ID объекта", required = true)
                                                 @PathVariable(value = "id") Long objectId) {
        return new ResponseEntity<>(storageObjectService.moveObject(storageObjectMove, objectId), HttpStatus.OK);
    }


    @PostMapping("/{id}/quantity")
    public ResponseEntity<StorageObjectDto> move(@RequestBody StorageObjectChangeQuantityDto changeQuantityDto,
                                                   @PathVariable(value = "id") Long objectId) {
        return new ResponseEntity<>(storageObjectService.changeQuantity(changeQuantityDto, objectId), HttpStatus.OK);
    }

    @Operation(
            summary = "Детали объекта",
            description = "Возвращает все данные по объекту, включая связи.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные объекта",
                            content = @Content(schema = @Schema(implementation = StorageObjectDtoResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Объект не найден")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<StorageObjectDtoResponse> findById(@Parameter(description = "ID объекта", required = true)
            @PathVariable(value = "id") Long storageId) {
        return new ResponseEntity<>(storageObjectService.findById(storageId), HttpStatus.OK);
    }

    @Operation(
            summary = "Список объектов",
            description = "Возвращает все объекты пагинацией.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список объектов",
                            content = @Content(schema = @Schema(implementation = StorageObjectDtoResponse.class)))
            }
    )
    @GetMapping
    public ResponseEntity<Page<StorageObjectDtoResponse>> findAll(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                storageObjectService.findAll(page, size),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Удаление объекта",
            description = "Удаляет объект из системы.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Объект удалён"),
                    @ApiResponse(responseCode = "403", description = "Нет доступа"),
                    @ApiResponse(responseCode = "404", description = "Объект не найден"),
                    @ApiResponse(responseCode = "409", description = "Нельзя удалить объект с ненулевым остатком")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID объекта", required = true)
            @PathVariable(value = "id") Long objectId) {
        storageObjectService.deleteById(objectId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
