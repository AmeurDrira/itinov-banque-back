package com.fr.itinov.banque.service;

import java.util.List;

import com.fr.itinov.banque.dto.DeviseDto;
import com.fr.itinov.banque.exception.FailedCreationException;
import com.fr.itinov.banque.exception.FailedUpdateException;
import com.fr.itinov.banque.exception.NotFoundException;
import com.fr.itinov.banque.model.Devise;
import com.fr.itinov.banque.repository.DeviseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviseService {

    private final DeviseRepository deviseRepository;

    @Transactional(readOnly = true)
    public List<Devise> getAllDevises() {
        log.debug("Récupération les Devises ");
        return deviseRepository.findAll();
    }

    @Transactional
    public Devise save(final Devise devise) {
        log.debug("cree un devise dans le service : {}", devise);
        if (deviseRepository.findById(devise.getCode()).isPresent()) {
            throw new FailedCreationException(String.format("Devise %s existe déja.", devise.getCode()));
        }
        return deviseRepository.save(devise);
    }

    @Transactional
    public Devise updateRate(final DeviseDto deviseDto) {
        log.debug("modifier un devise dans le service : {}", deviseDto);
        final Devise devise = deviseRepository.findById(deviseDto.getCode())
                .orElseThrow(() -> new FailedUpdateException(String.format("Devise %s n'existe pas.", deviseDto.getCode())));
        devise.setRate(deviseDto.getRate());
        return deviseRepository.save(devise);
    }

    @Transactional
    public void deleteByCode(final String deviseCode) {
        log.debug("Supprimer un devise dans le service par id : {}", deviseCode);
        deviseRepository.deleteById(deviseCode);
    }

    @Transactional(readOnly = true)
    public Devise findByCode(final String deviseCode) {
        log.debug("chercher un devise dans service par deviseName : {}", deviseCode);
        return deviseRepository.findById(deviseCode)
                .orElseThrow(() -> new NotFoundException(String.format("Devise %s n'existe pas.", deviseCode)));
    }
}
