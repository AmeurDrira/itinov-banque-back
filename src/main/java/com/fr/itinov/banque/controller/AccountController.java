package com.fr.itinov.banque.controller;

import java.util.List;

import com.fr.itinov.banque.dto.AccountDto;
import com.fr.itinov.banque.model.Account;
import com.fr.itinov.banque.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
@Tag(name = "account", description = "Endpoint exposé pour gérer les comptes")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/account-by-client/{clientId}")
    public List<Account> getAllAccountByClient(@PathVariable Long clientId) {
        return accountService.findAccountByClientId(clientId);
    }

    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable Long accountId) {
        return accountService.findById(accountId);
    }
    
    @PostMapping
    public Account create(@Valid @RequestBody AccountDto accountDto) {
        return accountService.save(accountDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        accountService.deleteById(id);
    }
    
}
