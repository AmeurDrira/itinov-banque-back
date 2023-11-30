package com.fr.itinov.banque.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "devise")
public class Devise {

    @Id
    @Column(name = "devise_code", unique = true, nullable = false)
    private String code;

    @Column(name = "devise_name", unique = true, nullable = false)
    private String name;

    @NotNull
    @Column(name = "devise_rate", precision = 12, scale = 4)
    private BigDecimal rate;
}
