package com.fr.itinov.banque.controller;

import java.util.List;

import com.fr.itinov.banque.dto.ClientDto;
import com.fr.itinov.banque.exception.NotFoundException;
import com.fr.itinov.banque.model.Client;
import com.fr.itinov.banque.service.ClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
@Tag(name = "client", description = "Endpoint exposé pour gérer les clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping(value = "/{id}")
    public Client getClient(@PathVariable(name = "id") Long id) {
        return clientService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("client %s n'existe pas.", id)));
    }

    @PostMapping
    public Client create(@Valid @RequestBody ClientDto clientDto) {
        return clientService.save(clientDto);
    }

    @PutMapping
    public Client updateClient( @Valid @RequestBody Client client) {
        return clientService.update(client);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        clientService.deleteById(id);
    }
}
