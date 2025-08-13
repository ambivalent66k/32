package org.example.storagesystem.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.cell.CellDto;
import org.example.storagesystem.dto.cell.CellPatchDto;
import org.example.storagesystem.service.CellService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("dev-api.storage-system.ru/v1/cells")
public class CellController {
    private final CellService cellService;

    @PostMapping()
    public ResponseEntity<CellDto> create(@Valid @RequestBody CellDto cellDto,
                                          BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return new ResponseEntity<>(cellService.createCell(cellDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CellDto> update(@Valid @RequestBody CellPatchDto cellPatchDto,
                                             @PathVariable(value = "id") Long cellId,
                                             BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(cellService.updateCell(cellPatchDto, cellId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CellDto> findById(@PathVariable(value = "id") Long storageId) {
        return new ResponseEntity<>(cellService.findWithChildrenById(storageId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<CellDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return new ResponseEntity<>(
                cellService.findAll(page, size),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(value = "id") Long cellId) {
        cellService.deleteById(cellId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
