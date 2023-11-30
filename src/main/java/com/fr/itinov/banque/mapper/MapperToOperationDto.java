package com.fr.itinov.banque.mapper;

import com.fr.itinov.banque.dto.OperationDto;
import com.fr.itinov.banque.model.Operation;
import lombok.experimental.UtilityClass;


@UtilityClass
public class MapperToOperationDto {

    public static OperationDto mapperToOperationDto(final Operation operation) {
        return OperationDto.builder()
                .clientId(operation.getClient().getId())
                .clientFirstName(operation.getClient().getFirstName())
                .clientLastName(operation.getClient().getLastName())
                .accountId(operation.getAccount().getId())
                .accountAutorisedDiscovery(operation.getAccount().getAutorisedDiscovery())
                .accountType(operation.getAccount().getType())
                .operationId(operation.getId())
                .operationType(operation.getType())
                .operationAmount(operation.getAmount())
                .operationBalanceAfter(operation.getBalanceAfter())
                .operationDateTime(operation.getDateTime())
                .build();
    }
}
