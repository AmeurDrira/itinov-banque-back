package com.fr.itinov.banque.repository;

import com.fr.itinov.banque.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
