package com.fr.itinov.banque.controller;

import java.util.List;

import com.fr.itinov.banque.dto.OperationDto;
import com.fr.itinov.banque.dto.RequestOperationDto;
import com.fr.itinov.banque.exception.NotFoundException;
import com.fr.itinov.banque.mapper.MapperToOperationDto;
import com.fr.itinov.banque.service.OperationService;
import com.fr.itinov.banque.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/operation")
@Tag(name = "operation", description = "Endpoint exposé pour gérer les operations")
public class OperationController {

    private final OperationService operationService;
    private final TransactionService transactionService;

    @GetMapping("/client/{clientId}")
    public List<OperationDto> getAllOperationsByClient(@PathVariable final Long clientId) {
        return operationService.getAllOperationsByClient(clientId);
    }

    @GetMapping("/{operationId}")
    public OperationDto getOperationsById(@PathVariable final Long operationId) {
        var operation = operationService.findOperationById(operationId)
                .orElseThrow(() -> new NotFoundException(String.format("Operation %s n'existe pas.", operationId)));
        return MapperToOperationDto.mapperToOperationDto(operation);
    }

    @PostMapping
    public List<OperationDto> createTransaction(@Valid @RequestBody final RequestOperationDto requestOperationDto) {
        return transactionService.createTransaction(requestOperationDto);
    }
}
