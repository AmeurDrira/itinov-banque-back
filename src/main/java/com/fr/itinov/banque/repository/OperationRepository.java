package com.fr.itinov.banque.repository;

import java.util.List;

import com.fr.itinov.banque.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findOperationByClient_Id(Long id);
}
