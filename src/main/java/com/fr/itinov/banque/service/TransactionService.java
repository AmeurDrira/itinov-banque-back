package com.fr.itinov.banque.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.fr.itinov.banque.dto.OperationDto;
import com.fr.itinov.banque.dto.RequestOperationDto;
import com.fr.itinov.banque.enums.OperationType;
import com.fr.itinov.banque.exception.OperationException;
import com.fr.itinov.banque.mapper.MapperToOperationDto;
import com.fr.itinov.banque.model.Account;
import com.fr.itinov.banque.model.Devise;
import com.fr.itinov.banque.model.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private static final String EUR = "EUR";
    private final AccountService accountService;
    private final DeviseService deviseService;
    private final OperationService operationService;


    public List<OperationDto> createTransaction(final RequestOperationDto requestOperationDto) {
        log.debug("cree une operation dans le service : {}", requestOperationDto);
        final BigDecimal amountEuro = getAmountEuro(requestOperationDto);
        final Account accountRecevier = accountService.findById(requestOperationDto.getAccountReceiverId());

        return switch (requestOperationDto.getType()) {
            case DEPOT -> Stream.of(depositOperation(accountRecevier, amountEuro, OperationType.DEPOT))
                    .map(MapperToOperationDto::mapperToOperationDto)
                    .toList();
            case RETRAIT -> Stream.of(withdrawalOperation(accountRecevier, amountEuro, OperationType.RETRAIT))
                    .map(MapperToOperationDto::mapperToOperationDto)
                    .toList();
            case VIREMENT -> {
                final Account accountSource = accountService.findById(requestOperationDto.getAccountSourceId());
                yield virementOperation(accountRecevier, accountSource, amountEuro);
            }
        };
    }

    private static Operation buildOperation(BigDecimal amountEuro, Account accountRecevier) {
        return Operation.builder()
                .client(accountRecevier.getClient())
                .account(accountRecevier)
                .dateTime(LocalDateTime.now())
                .amount(amountEuro)
                .build();
    }

    private BigDecimal getAmountEuro(RequestOperationDto requestOperationDto) {
        return Objects.equals(EUR, requestOperationDto.getDeviseCode())
                ? requestOperationDto.getAmount()
                : getAnountChangeRate(requestOperationDto, requestOperationDto.getDeviseCode());
    }

    private BigDecimal getAnountChangeRate(RequestOperationDto requestOperationDto, String deviseCode) {
        final Devise devise = deviseService.findByCode(deviseCode);
        return devise.getRate().multiply(requestOperationDto.getAmount());
    }

    private Operation depositOperation(final Account accountRecevier,
                                       final BigDecimal amountEuro, OperationType operationType) {
        final Operation operation = buildOperation(amountEuro, accountRecevier);
        accountRecevier.setBalance(accountRecevier.getBalance().add(amountEuro));
        accountService.update(accountRecevier);
        operation.setType(operationType);
        operation.setBalanceAfter(accountRecevier.getBalance());
        return operationService.saveOperation(operation);
    }

    private Operation withdrawalOperation(final Account accountRecevier,
                                          final BigDecimal amountEuro, OperationType operationType) {
        final BigDecimal newSolde = accountRecevier.getBalance().subtract(amountEuro);
        if (newSolde.compareTo(accountRecevier.getAutorisedDiscovery().negate()) < 0) {
            throw new OperationException(String.format("dépassement du seuil de découvert %s",
                    newSolde.add(accountRecevier.getAutorisedDiscovery())));
        }
        final Operation operation = buildOperation(amountEuro, accountRecevier);
        accountRecevier.setBalance(newSolde);
        accountService.update(accountRecevier);
        operation.setType(operationType);
        operation.setBalanceAfter(accountRecevier.getBalance());
        return operationService.saveOperation(operation);
    }

    private List<OperationDto> virementOperation(final Account accountRecevier, final Account accountSource,
                                                 final BigDecimal amountEuro) {
        var withdrawalOperation = withdrawalOperation(accountSource, amountEuro, OperationType.VIREMENT);
        var depositOperation = depositOperation(accountRecevier, amountEuro, OperationType.VIREMENT);
        return Stream.of(withdrawalOperation, depositOperation)
                .map(MapperToOperationDto::mapperToOperationDto)
                .toList();
    }


}
