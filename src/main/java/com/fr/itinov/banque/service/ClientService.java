package com.fr.itinov.banque.service;

import java.util.List;
import java.util.Optional;

import com.fr.itinov.banque.dto.ClientDto;
import com.fr.itinov.banque.exception.FailedUpdateException;
import com.fr.itinov.banque.model.Client;
import com.fr.itinov.banque.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        log.debug("Récupération les Clients ");
        return clientRepository.findAll();
    }

    @Transactional
    public Client update(final Client client) {
        log.debug("modifier un client dans le service : {}", client);
        if ((null == client.getId()) || findById(client.getId()).isEmpty()) {
            throw new FailedUpdateException(String.format("client %s n'existe pas.", client.getId()));
        }
        return clientRepository.save(client);
    }

    @Transactional
    public Client save(final ClientDto clientDto) {
        log.debug("cree un client dans le service : {}", clientDto);
        return clientRepository.save(Client.builder()
                .lastName(clientDto.getLastName())
                .firstName(clientDto.getFirstName())
                .build());
    }

    @Transactional
    public void deleteById(final Long id) {
        log.debug("Supprimer un client dans le service par id : {}", id);
        clientRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Client> findById(final Long id) {
        log.debug("chercher un client dans service par id : {}", id);
        return clientRepository.findById(id);
    }
}
