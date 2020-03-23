package com.lazoski.jsonpath;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

public final class App {
    private App() {
    }

    public static void main(String[] args) throws Exception {
        //configuração do serializador/desserializador Jackson
        ObjectMapper mapper = new ObjectMapper();
        //tratamento de datas
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //ignorar campos desconhecidos
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Plano plano = criarNovoPlano();
        System.out.println("Novo plano criado " + plano);

        String json = mapper.writeValueAsString(plano);
        System.out.println("Plano convertido para JSON " + json);

        //configuração do jsonpath para usar provedores Jackson
        Configuration configuration = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .mappingProvider(new JacksonMappingProvider(mapper))
            .build();
        DocumentContext ctx = JsonPath.using(configuration).parse(json);

        List<Comando> comandos = Arrays.asList(
            new Comando("A", "$.id", "id_alterado"),
            new Comando("A", "$.precoMinimo", new BigDecimal("40.85")),
            new Comando("A", "$.ajustes[?(@['data'] == '2020-01-01')].motivo", "motivo alterado utilizando filtro de data"),
            new Comando("D", "$.ajustes[?(@['data'] == '2020-01-02')]", null)
        );

        comandos.stream().forEach(c -> {
            Object valor = ctx.read(c.getJsonPath());
            System.out.println("Valor original: " + valor);
            if(c.getTipo() == "A") {
                ctx.set(c.getJsonPath(), c.getValor());
            } else if(c.getTipo() == "D") {
                ctx.delete(c.getJsonPath());
            } else if(c.getTipo() == "I") {
                ctx.add(c.getJsonPath(), mapper.createObjectNode());
            }
        });

        ctx.add("$.ajustes", mapper.createObjectNode());
        ctx.put("$.ajustes[-1:]", "motivo", "motivo do novo item");
        ctx.put("$.ajustes[-1:]", "preco", new BigDecimal("1010.33"));
        ctx.put("$.ajustes[-1:]", "data", "2020-04-01");

        String jsonAlterado = ctx.jsonString();
        System.out.println("JSON com alterações: " + jsonAlterado);
        Plano plano2 = mapper.readValue(jsonAlterado, Plano.class);
        System.out.println("Plano voltou a ser objeto Java, com alterações: " + plano2);
    }

    private static Plano criarNovoPlano() {
        Ajuste ajuste1 = new Ajuste(new BigDecimal("30.15"), "motivo do primeiro ajuste", LocalDate.parse("2020-01-01"));
        Ajuste ajuste2 = new Ajuste(new BigDecimal("40.15"), "motivo do segundo ajuste", LocalDate.parse("2020-01-02"));
        Ajuste ajuste3 = new Ajuste(new BigDecimal("50.15"), "motivo do terceiro ajuste", LocalDate.parse("2020-01-03"));

        Plano plano = new Plano("id_original", new BigDecimal("25.47"), Arrays.asList(ajuste1, ajuste2, ajuste3));
        return plano;
    }
}
