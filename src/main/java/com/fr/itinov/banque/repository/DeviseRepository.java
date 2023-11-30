package com.fr.itinov.banque.repository;

import com.fr.itinov.banque.model.Devise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviseRepository extends JpaRepository<Devise, String> {

}
