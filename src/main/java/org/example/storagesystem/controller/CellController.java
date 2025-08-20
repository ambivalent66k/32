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
import org.example.storagesystem.dto.cell.CellDto;
import org.example.storagesystem.dto.cell.CellMoveDto;
import org.example.storagesystem.dto.cell.CellPatchDto;
import org.example.storagesystem.dto.cell.response.CellDtoResponse;
import org.example.storagesystem.dto.cell.response.CellDtoResponseOnMove;
import org.example.storagesystem.dto.cell.response.CellDtosResponse;
import org.example.storagesystem.service.CellService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cells")
@Tag(name = "Cells", description = "Управление ячейками хранения")
public class CellController {
    private final CellService cellService;

    @Operation(
            summary = "Создание ячейки",
            description = "Создаёт новую ячейку в указанном хранилище с возможностью вложенности",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ячейка создана",
                            content = @Content(schema = @Schema(implementation = CellDto.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
                    @ApiResponse(responseCode = "409", description = "Превышена вместимость родительской ячейки")
            }
    )
    @PostMapping()
    public ResponseEntity<CellDto> create(@Valid @Parameter(description = "Данные новой ячейки",
                                                      required = true, content = @Content(
                                                              schema = @Schema(implementation = CellDto.class)))
                                              @RequestBody CellDto cellDto,
                                          BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return new ResponseEntity<>(cellService.createCell(cellDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновление ячейки",
            description = "Изменяет атрибуты ячейки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ячейка обновлена",
                            content = @Content(schema = @Schema(implementation = CellDto.class))),
                    @ApiResponse(responseCode = "409", description = "Превышена вместимость ячейки")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<CellDto> update(@Valid @Parameter(description = "Изменяемые данные ячейки",
                                                      required = true, content = @Content(
                                                              schema = @Schema(implementation = CellPatchDto.class)))
                                              @RequestBody CellPatchDto cellPatchDto,
                                          @Parameter(description = "ID ячейки", required = true)
                                          @PathVariable(value = "id") Long cellId,
                                             BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(cellService.updateCell(cellPatchDto, cellId), HttpStatus.OK);
    }

    @Operation(
            summary = "Перемещение ячейки",
            description = "Выполняет перемещение ячейки в указанное хранилище и/или ячейку с доменными проверками",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ячейка перемещена",
                            content = @Content(schema = @Schema(implementation = CellDtoResponseOnMove.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры"),
                    @ApiResponse(responseCode = "404", description = "Не найдено"),
                    @ApiResponse(responseCode = "409", description = "Конфликт доменных ограничений")
            }
    )
    @PostMapping("/{id}/move")
    public ResponseEntity<CellDtoResponseOnMove> move(@RequestBody @Parameter(description = "Параметры перемещения",
                                                                  required = true,
                                                                  content = @Content(
                                                                          schema = @Schema(
                                                                                  implementation = CellMoveDto.class)))
                                                          CellMoveDto cellMoveDto,
                                                      @Parameter(description = "ID ячейки", required = true)
                                                      @PathVariable(value = "id") Long cellId) {
        return new ResponseEntity<>(cellService.moveCell(cellMoveDto, cellId), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Детали ячейки",
            description = "Возвращает информацию о ячейке и её содержимом",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные ячейки",
                            content = @Content(schema = @Schema(implementation = CellDtoResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Ячейка не найдена")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CellDtoResponse> findById(@Parameter(description = "ID ячейки", required = true)
            @PathVariable(value = "id") Long storageId) {
        return new ResponseEntity<>(cellService.findById(storageId), HttpStatus.OK);
    }

    @Operation(
            summary = "Список ячеек",
            description = "Возвращает все ячейки с пагинацией",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список ячеек",
                            content = @Content(array = @ArraySchema(
                                    schema = @Schema(implementation = CellDtosResponse.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<CellDtosResponse>> findAll(
            @Parameter(description = "Номер страницы", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "10") @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                cellService.findAll(page, size),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Удаление ячейки",
            description = "Удаляет пустую ячейку",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Ячейка удалена"),
                    @ApiResponse(responseCode = "404", description = "Не найдено"),
                    @ApiResponse(responseCode = "409", description = "Нельзя удалить непустую ячейку")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID ячейки", required = true)
            @PathVariable(value = "id") Long cellId) {
        cellService.deleteById(cellId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
