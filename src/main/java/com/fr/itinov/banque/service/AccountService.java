package com.fr.itinov.banque.service;

import java.util.List;

import com.fr.itinov.banque.dto.AccountDto;
import com.fr.itinov.banque.exception.FailedCreationException;
import com.fr.itinov.banque.exception.NotFoundException;
import com.fr.itinov.banque.model.Account;
import com.fr.itinov.banque.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final ClientService clientService;
    private final AccountRepository accountRepository;

    @Transactional
    public Account save(final AccountDto accountDto) {
        log.debug("cree un compte dans le service : {}", accountDto);
        final var client = clientService.findById(accountDto.getClientId());
        if (client.isEmpty()) {
            throw new FailedCreationException(String.format("client %s n'existe pas.", accountDto.getClientId()));
        }
        return accountRepository.save(Account.builder()
                .balance(accountDto.getBalance())
                .creationDateTime(accountDto.getCreationDateTime())
                .client(client.get())
                .type(accountDto.getType())
                .autorisedDiscovery(accountDto.getAutorisedDiscovery())
                .build());
    }

    @Transactional
    public Account update(final Account account) {
        log.debug("mettre a jour un compte dans le service : {}", account);
        return accountRepository.save(account);
    }

    @Transactional
    public void deleteById(final Long id) {
        log.debug("Supprimer un compte dans le service par id : {}", id);
        accountRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Account findById(final Long id) {
        log.debug("chercher un compte dans service par id : {}", id);
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("compte id : %s n'existe pas.", id)));
    }

    @Transactional(readOnly = true)
    public List<Account> findAccountByClientId(final Long clientId) {
        log.debug("chercher un compte dans service par client id : {}", clientId);
        return accountRepository.findAccountsByClient_Id(clientId);
    }
}
