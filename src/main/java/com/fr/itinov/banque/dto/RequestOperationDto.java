package com.fr.itinov.banque.dto;


import java.math.BigDecimal;

import com.fr.itinov.banque.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOperationDto {

    @NotNull
    private Long accountReceiverId;

    private Long accountSourceId;

    @Positive
    private BigDecimal amount;

    @NotNull
    private OperationType type;

    @NotNull
    private String deviseCode;


}
