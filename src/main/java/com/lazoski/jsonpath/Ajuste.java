package com.lazoski.jsonpath;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class Ajuste {
    private BigDecimal preco;
    private String motivo;
    private LocalDate data;
}
