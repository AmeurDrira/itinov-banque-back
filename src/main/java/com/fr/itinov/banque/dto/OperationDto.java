package com.fr.itinov.banque.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fr.itinov.banque.enums.AccountType;
import com.fr.itinov.banque.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationDto {

    private Long clientId;
    private String clientFirstName;
    private String clientLastName;

    private Long accountId;
    private BigDecimal accountAutorisedDiscovery;
    private AccountType accountType;

    private Long operationId;
    private OperationType operationType;
    private BigDecimal operationAmount;
    private BigDecimal operationBalanceAfter;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime operationDateTime;
}
