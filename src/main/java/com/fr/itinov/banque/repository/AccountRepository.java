package com.fr.itinov.banque.repository;

import java.util.List;

import com.fr.itinov.banque.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAccountsByClient_Id(Long clientId);
}
