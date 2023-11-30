package com.fr.itinov.banque.controller;

import java.util.List;

import com.fr.itinov.banque.dto.DeviseDto;
import com.fr.itinov.banque.model.Devise;
import com.fr.itinov.banque.service.DeviseService;
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
@RequestMapping("/api/devise")
@Tag(name = "devise", description = "Endpoint exposé pour gérer les devises")
public class DeviseController {

    private final DeviseService deviseService;

    @GetMapping
    public List<Devise> getAllDevises() {
        return deviseService.getAllDevises();
    }

    @GetMapping(value = "/{code}")
    public Devise getDeviseByCode(@PathVariable(name = "code") String code) {
        return deviseService.findByCode(code);
    }

    @PostMapping
    public Devise create(@Valid @RequestBody Devise devise) {
        return deviseService.save(devise);
    }

    @PutMapping
    public Devise updateRate(@Valid @RequestBody DeviseDto deviseDto) {
        return deviseService.updateRate(deviseDto);
    }

    @DeleteMapping(value = "/{code}")
    public void delete(@PathVariable(name = "code") String code) {
        deviseService.deleteByCode(code);
    }
}
