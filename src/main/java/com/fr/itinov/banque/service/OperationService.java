package com.fr.itinov.banque.service;

import java.util.List;
import java.util.Optional;

import com.fr.itinov.banque.dto.OperationDto;
import com.fr.itinov.banque.mapper.MapperToOperationDto;
import com.fr.itinov.banque.model.Operation;
import com.fr.itinov.banque.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;

    @Transactional(readOnly = true)
    public List<OperationDto> getAllOperationsByClient(final Long clientId) {
        log.debug("Récupération les operations by client");
        return operationRepository.findOperationByClient_Id(clientId).stream()
                .map(MapperToOperationDto::mapperToOperationDto)
                .toList();
    }

    @Transactional
    public Operation saveOperation(final Operation operation) {
        return operationRepository.save(operation);
    }

    @Transactional(readOnly = true)
    public Optional<Operation> findOperationById(final Long operationId) {
        log.debug("chercher une operation dans service par id : {}", operationId);
        return operationRepository.findById(operationId);
    }

}
