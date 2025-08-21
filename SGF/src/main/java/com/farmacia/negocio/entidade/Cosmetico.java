package com.farmacia.negocio.entidade;

import java.util.Arrays;
import java.util.List;

public class Cosmetico extends Produto {
    private static final List<String> TIPOS_DE_USO = Arrays.asList(
            "Facial", "Corporal", "Capilar", "Unhas", "Maquiagem"
    );

    private final String tipoDeUso;

    public Cosmetico(String nome, double preco, String fabricante, String tipoDeUso) {
        super(nome, preco, fabricante);

        if (!isTipoDeUsoValido(tipoDeUso)) {
            //Tipo de uso inválido
        }
        this.tipoDeUso = tipoDeUso;
    }

    private boolean isTipoDeUsoValido(String tipoDeUso) {
        if (tipoDeUso == null || tipoDeUso.trim().isEmpty()) {
            return false;
        }
        for (String valido : TIPOS_DE_USO) {
            if (valido.equalsIgnoreCase(tipoDeUso.trim())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int calcularPontosGerados(int quantidadeVendida) {
        int pontosBase;

        if ("Facial".equalsIgnoreCase(this.tipoDeUso) || "Maquiagem".equalsIgnoreCase(this.tipoDeUso)) {
            pontosBase = 15;
        } else if ("Capilar".equalsIgnoreCase(this.tipoDeUso) || "Corporal".equalsIgnoreCase(this.tipoDeUso)) {
            pontosBase = 10;
        } else if ("Unhas".equalsIgnoreCase(this.tipoDeUso)) {
            pontosBase = 8;
        } else {
            pontosBase = 10; // Padrão
        }

        return pontosBase * quantidadeVendida;
    }

    public String getTipoDeUso() {
        return tipoDeUso;
    }

    @Override
    public String toString() {
        return super.toString() + " | Categoria: Cosmético (" + tipoDeUso + ")";
    }
}