package org.example.storagesystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.cell.response.CellDtosResponse;
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
@Tag(name = "Storages", description = "Управление хранилищами")
public class StorageController {
    private final StorageService storageService;

    @Operation(
            summary = "Создание хранилища",
            description = "Создаёт новое хранилище с именем, описанием и местоположением.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Хранилище создано",
                            content = @Content(schema = @Schema(implementation = StorageDto.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            }
    )
    @PostMapping()
    public ResponseEntity<StorageDto> create(@Valid @Parameter(description = "Данные нового хранилища", required = true,
            content = @Content(schema = @Schema(implementation = StorageDto.class)))
                                                 @RequestBody() StorageDto storageDto,
                                           BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(storageService.createStorage(storageDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновление хранилища",
            description = "Изменяет атрибуты хранилища.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Хранилище обновлено",
                            content = @Content(schema = @Schema(implementation = StorageDto.class))),
                    @ApiResponse(responseCode = "404", description = "Хранилище не найдено")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<StorageDto> update(@Valid @Parameter(description = "Изменяемые данные хранилища",
                                                         content = @Content(
                                                                 schema = @Schema(
                                                                         implementation = StoragePatchDto.class)))
                                                 @RequestBody StoragePatchDto storagePatchDto,
                                             @Parameter(description = "ID хранилища", required = true)
                                             @PathVariable(value = "id") Long storageId,
                                             BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(storageService.updateStorage(storagePatchDto, storageId), HttpStatus.OK);
    }

    @Operation(
            summary = "Перемещение хранилища (администратор)",
            description = "Выполняет перемещение хранилища в указанное хранилище с доменными проверками. " +
                    "Остальные поля хранилища не изменяются.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Хранилище перемещено",
                            content = @Content(schema = @Schema(implementation = StorageDtoResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры"),
                    @ApiResponse(responseCode = "404", description = "Не найдено"),
                    @ApiResponse(responseCode = "409", description = "Конфликт доменных ограничений")
            }
    )
    @PostMapping("/{id}/move")
    public ResponseEntity<StorageDtoResponse> move(@Parameter(description = "Параметры перемещения хранилища",
                                                               required = true,
                                                               content = @Content(schema =
                                                               @Schema(implementation = StorageMoveDto.class)))
                                                       @RequestBody StorageMoveDto storageMoveDto,
                                                   @Parameter(description = "ID хранилища", required = true)
                                                   @PathVariable(value = "id") Long storageId){
        return new ResponseEntity<>(storageService.moveStorage(storageMoveDto, storageId), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Детали хранилища",
            description = "Возвращает информацию о хранилище, включая вложенные ячейки и объекты.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные хранилища",
                            content = @Content(schema = @Schema(implementation = StorageDtoResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Хранилище не найдено")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<StorageDtoResponse> findById(@Parameter(description = "ID хранилища", required = true)
                                                           @PathVariable(value = "id") Long storageId) {
        return new ResponseEntity<>(storageService.findById(storageId), HttpStatus.OK);
    }


    @Operation(
            summary = "Список хранилищ",
            description = "Возвращает все хранилища с пагинацией",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список хранилищ",
                            content = @Content(array = @ArraySchema(
                                    schema = @Schema(implementation = CellDtosResponse.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<StorageDtosResponse>> findAll(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                storageService.findAll(page, size),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Удаление хранилища (администратор)",
            description = "Удаляет пустое хранилище.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Хранилище удалено"),
                    @ApiResponse(responseCode = "404", description = "Хранилище не найдено"),
                    @ApiResponse(responseCode = "409", description = "Нельзя удалить непустое хранилище")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID хранилища", required = true)
            @PathVariable(value = "id") Long storageId) {
        storageService.deleteById(storageId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
