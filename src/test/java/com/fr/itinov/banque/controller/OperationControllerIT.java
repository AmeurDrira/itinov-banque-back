package com.fr.itinov.banque.controller;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fr.itinov.banque.config.IntegrationTest;
import com.fr.itinov.banque.data.Data;
import com.fr.itinov.banque.dto.RequestOperationDto;
import com.fr.itinov.banque.enums.OperationType;
import com.fr.itinov.banque.repository.AccountRepository;
import com.fr.itinov.banque.repository.ClientRepository;
import com.fr.itinov.banque.repository.DeviseRepository;
import com.fr.itinov.banque.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OperationControllerIT {

    private static final String URL_OPERATION = "/api/operation";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private DeviseRepository deviseRepository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUpAll() {
        deviseRepository.deleteAll();
        operationRepository.deleteAll();
        accountRepository.deleteAll();
        clientRepository.deleteAll();

        deviseRepository.saveAll(Data.getDevises());
        clientRepository.saveAll(Data.getClients());
        accountRepository.saveAll(Data.getAccounts());
        operationRepository.saveAll(Data.getOperations());
    }

    @Test
    void return_list_operation_dto_when_get_operation_by_client() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL_OPERATION + "/client/1")
                .accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void return_operation_when_find_operation_by_id() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL_OPERATION + "/2")
                .accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientFirstName").value("ervan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientLastName").value("salin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountAutorisedDiscovery").value(500.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountType").value("CC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationType").value("RETRAIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationAmount").value(10.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationBalanceAfter").value(500.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.operationDateTime").value("2023-11-30 14:30:00"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void throw_not_found_exception_when_get_operation_by_id() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL_OPERATION + "/999")
                .accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.detail", is("Not found: Operation 999 n'existe pas.")))
                .andExpect(jsonPath("$.status", is(404)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void return_operation_dto_when_deposit() throws Exception {
        var operationDto = RequestOperationDto.builder()
                .accountReceiverId(1L)
                .deviseCode("EUR")
                .amount(BigDecimal.valueOf(200))
                .type(OperationType.DEPOT)
                .build();

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_OPERATION)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(operationDto))
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientFirstName").value("ervan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientLastName").value("salin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountAutorisedDiscovery").value(500.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountType").value("CC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationType").value("DEPOT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationAmount").value(200.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationBalanceAfter").value(1550.00))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void return_operation_dto_when_deposit_with_USD() throws Exception {
        var operationDto = RequestOperationDto.builder()
                .accountReceiverId(1L)
                .deviseCode("USD")
                .amount(BigDecimal.valueOf(100))
                .type(OperationType.DEPOT)
                .build();

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_OPERATION)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(operationDto))
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationAmount").value(85.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationBalanceAfter").value(1435))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void return_operation_dto_when_withdrawal() throws Exception {
        var operationDto = RequestOperationDto.builder()
                .accountReceiverId(1L)
                .deviseCode("EUR")
                .amount(BigDecimal.valueOf(100))
                .type(OperationType.RETRAIT)
                .build();

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_OPERATION)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(operationDto))
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientFirstName").value("ervan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientLastName").value("salin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountAutorisedDiscovery").value(500.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountType").value("CC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationType").value("RETRAIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationAmount").value(100.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationBalanceAfter").value(1250.00))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void throw_operation_exception_when_account_overdrafted() throws Exception {
        var operationDto = RequestOperationDto.builder()
                .accountReceiverId(1L)
                .deviseCode("EUR")
                .amount(BigDecimal.valueOf(2000))
                .type(OperationType.RETRAIT)
                .build();

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_OPERATION)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(operationDto))
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.detail", is("Operation refusé: dépassement du seuil de découvert -150.00")))
                .andExpect(jsonPath("$.status", is(400)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void return_operation_dto_when_virement() throws Exception {
        var operationDto = RequestOperationDto.builder()
                .accountReceiverId(2L)
                .accountSourceId(1L)
                .deviseCode("EUR")
                .amount(BigDecimal.valueOf(150))
                .type(OperationType.VIREMENT)
                .build();

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_OPERATION)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(operationDto))
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientFirstName").value("ervan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientLastName").value("salin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountAutorisedDiscovery").value(500.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountType").value("CC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationType").value("VIREMENT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationAmount").value(150.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].operationBalanceAfter").value(1200.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].clientId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].clientFirstName").value("ervan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].clientLastName").value("salin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountAutorisedDiscovery").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountType").value("LA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].operationId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].operationType").value("VIREMENT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].operationAmount").value(150.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].operationBalanceAfter").value(1650.00))
                .andDo(MockMvcResultHandlers.print());
    }
}
