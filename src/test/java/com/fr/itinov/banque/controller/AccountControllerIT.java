package com.fr.itinov.banque.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fr.itinov.banque.config.IntegrationTest;
import com.fr.itinov.banque.data.Data;
import com.fr.itinov.banque.dto.AccountDto;
import com.fr.itinov.banque.enums.AccountType;
import com.fr.itinov.banque.repository.AccountRepository;
import com.fr.itinov.banque.repository.ClientRepository;
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
class AccountControllerIT {

    private static final String URL_ACCOUNT = "/api/account";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUpAll() {
        repository.deleteAll();
        clientRepository.deleteAll();
        clientRepository.saveAll(Data.getClients());
        repository.saveAll(Data.getAccounts());
    }

    @Test
    void return_list_account_when_get_account_by_client() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL_ACCOUNT + "/account-by-client/1")
                .accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void return_account_when_save_account() throws Exception {
        var account = AccountDto.builder()
                .type(AccountType.LDD)
                .autorisedDiscovery(BigDecimal.ZERO)
                .balance(new BigDecimal("100.00"))
                .creationDateTime(LocalDateTime.of(2023, 11, 30, 14, 30, 0))
                .clientId(1L)
                .build();
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_ACCOUNT)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(account))
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("LDD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.autorisedDiscovery").value(new BigDecimal("0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(new BigDecimal("100.0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDateTime").value("2023-11-30 14:30:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void throw_failed_creation_exception_when_save_account_with_client_not_exist() throws Exception {
        var account = AccountDto.builder()
                .type(AccountType.LDD)
                .autorisedDiscovery(BigDecimal.ZERO)
                .balance(new BigDecimal("100.00"))
                .creationDateTime(LocalDateTime.of(2023, 11, 30, 14, 30, 0))
                .clientId(999L)
                .build();
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_ACCOUNT)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(account))
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.detail", is("Creation échouée: client 999 n'existe pas.")))
                .andExpect(jsonPath("$.status", is(400)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void return_void_when_delete_client_by_id() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(URL_ACCOUNT + "/2")
                .accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void return_account_when_find_account_by_id() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL_ACCOUNT + "/2")
                .accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("LA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDateTime").value("2023-11-29 14:30:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(1500.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2L))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void throw_not_found_exception_when_get_account_by_id() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL_ACCOUNT + "/999")
                .accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.detail", is("Not found: compte id : 999 n'existe pas.")))
                .andExpect(jsonPath("$.status", is(404)))
                .andDo(MockMvcResultHandlers.print());
    }
}
