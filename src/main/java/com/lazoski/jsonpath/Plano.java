package com.lazoski.jsonpath;

import java.math.BigDecimal;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor
@ToString
public class Plano {
    private String id;
    private BigDecimal precoMinimo;
    // private LocalDate criadoEm;
    private Collection<Ajuste> ajustes;

    public String getQQCoisa() {
        return id + precoMinimo;
    }
}