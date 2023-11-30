package com.fr.itinov.banque.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fr.itinov.banque.enums.AccountType;
import com.fr.itinov.banque.enums.OperationType;
import com.fr.itinov.banque.model.Account;
import com.fr.itinov.banque.model.Client;
import com.fr.itinov.banque.model.Devise;
import com.fr.itinov.banque.model.Operation;

public class Data {

    public static List<Client> getClients() {
        return List.of(
                Client.builder()
                        .id(1L)
                        .firstName("ervan")
                        .lastName("salin")
                        .build(),
                Client.builder()
                        .id(2L)
                        .firstName("jean-paul")
                        .lastName("gaultier")
                        .build(),
                Client.builder()
                        .id(3L)
                        .firstName("terry")
                        .lastName("Harmon")
                        .build()

        );
    }

    public static List<Account> getAccounts() {
        return List.of(Account.builder()
                        .id(1L)
                        .type(AccountType.CC)
                        .autorisedDiscovery(BigDecimal.valueOf(500.0))
                        .balance(BigDecimal.valueOf(1350))
                        .creationDateTime(LocalDateTime.of(2023, 11, 29, 14, 30, 0))
                        .client(getClients().get(0))
                        .build(),
                Account.builder()
                        .id(2L)
                        .type(AccountType.LA)
                        .autorisedDiscovery(BigDecimal.ZERO)
                        .balance(BigDecimal.valueOf(1500))
                        .creationDateTime(LocalDateTime.of(2023, 11, 29, 14, 30, 0))
                        .client(getClients().get(0))
                        .build());
    }

    public static List<Operation> getOperations() {
        return List.of(Operation.builder()
                        .id(1L)
                        .account(getAccounts().get(0))
                        .amount(BigDecimal.valueOf(510))
                        .client(getClients().get(0))
                        .dateTime(LocalDateTime.of(2023, 11, 30, 14, 30, 0))
                        .balanceAfter(BigDecimal.valueOf(510))
                        .type(OperationType.DEPOT)
                        .build(),
                Operation.builder()
                        .id(2L)
                        .account(getAccounts().get(0))
                        .amount(BigDecimal.TEN)
                        .client(getClients().get(0))
                        .dateTime(LocalDateTime.of(2023, 11, 30, 14, 30, 0))
                        .balanceAfter(BigDecimal.valueOf(500))
                        .type(OperationType.RETRAIT)
                        .build()
        );
    }

    public static List<Devise> getDevises() {
        return List.of(Devise.builder()
                        .code("USD")
                        .name("DOLLAR")
                        .rate(new BigDecimal("0.8500"))
                        .build(),
                Devise.builder()
                        .code("GBP")
                        .name("LIVRE_STERLING")
                        .rate(new BigDecimal("1.1500"))
                        .build());
    }
}

