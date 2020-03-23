package com.lazoski.jsonpath;

import lombok.Getter;

@Getter
public class Comando {

	private String tipo;
    private String jsonPath;
    private Object valor;

    public Comando(String tipo, String jsonPath, Object valor) {
        this.tipo = tipo;
        this.jsonPath = jsonPath;
        this.valor = valor;
	}

}
