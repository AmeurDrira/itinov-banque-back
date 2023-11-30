package com.fr.itinov.banque.controller;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fr.itinov.banque.config.IntegrationTest;
import com.fr.itinov.banque.data.Data;
import com.fr.itinov.banque.dto.DeviseDto;
import com.fr.itinov.banque.model.Devise;
import com.fr.itinov.banque.repository.DeviseRepository;
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
class DeviseControllerIT {

    private static final String URL_DEVISE = "/api/devise";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviseRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUpAll() {
        repository.deleteAll();
        repository.saveAll(Data.getDevises());
    }

    @Test
    void return_list_devise_when_get_all_devises() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL_DEVISE)
                .accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void return_devise_when_save_devise() throws Exception {
        var devise = Devise.builder()
                .code("CHF")
                .name("FRANC_SUISSE")
                .rate(new BigDecimal("0.96"))
                .build();
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_DEVISE)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(devise))
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("CHF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("FRANC_SUISSE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rate").value(new BigDecimal("0.96")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void throw_failed_creation_when_save_exist_devise() throws Exception {
        var devise = Devise.builder()
                .code("USD")
                .name("DOLLAR")
                .rate(new BigDecimal("0.85"))
                .build();
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_DEVISE)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(devise))
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.detail", is("Creation échouée: Devise USD existe déja.")))
                .andExpect(jsonPath("$.status", is(400)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void return_void_when_delete_devise_by_id() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(URL_DEVISE + "/USD")
                .accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void return_devise_when_update_devise_by_code() throws Exception {
        var devise = DeviseDto.builder()
                .code("GBP")
                .rate(new BigDecimal("1.18"))
                .build();
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.put(URL_DEVISE)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(devise))
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("GBP"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("LIVRE_STERLING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rate").value(new BigDecimal("1.18")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void throw_failed_update_exception_when_update_devise() throws Exception {
        var devise = DeviseDto.builder()
                .code("XXX")
                .rate(BigDecimal.valueOf(1, 18))
                .build();
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.put(URL_DEVISE)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(devise))
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.detail", is("Modification échouée: Devise XXX n'existe pas.")))
                .andExpect(jsonPath("$.status", is(400)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void return_devise_when_find_devise_by_Code() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL_DEVISE + "/USD")
                .accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("USD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("DOLLAR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rate").value(new BigDecimal("0.85")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void throw_not_found_exception_when_get_devise() throws Exception {
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URL_DEVISE + "/XXX")
                .accept(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder).andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.detail", is("Not found: Devise XXX n'existe pas.")))
                .andExpect(jsonPath("$.status", is(404)))
                .andDo(MockMvcResultHandlers.print());
    }
}
