package com.farmacia.negocio.entidade;

import java.util.Arrays;
import java.util.List;

public class Cosmetico extends Produto {
    private static final List<String> TIPOS_DE_USO = Arrays.asList(
            "Facial", "Corporal", "Capilar", "Unhas", "Maquiagem"
    );

    private final String tipoDeUso;

    public Cosmetico(String nome, double preco, String fabricante, int estoqueMinimo, String tipoDeUso) {
        super(nome, preco, fabricante, estoqueMinimo);

        if (!isTipoDeUsoValido(tipoDeUso)) {
            throw new IllegalArgumentException(
                    "Tipo de uso inválido: '" + tipoDeUso + "'. Valores aceitos são: " + TIPOS_DE_USO
            );
        }
        this.tipoDeUso = tipoDeUso;
    }

    private boolean isTipoDeUsoValido(String tipoDeUso) {
        if (tipoDeUso == null || tipoDeUso.trim().isEmpty()) return false;
        for (String valido : TIPOS_DE_USO) {
            if (valido.equalsIgnoreCase(tipoDeUso.trim())) return true;
        }
        return false;
    }

    @Override
    public int calcularPontosGerados(int quantidadeVendida) {
        int pontosBase;

        if ("Facial".equalsIgnoreCase(this.tipoDeUso) || "Maquiagem".equalsIgnoreCase(this.tipoDeUso)) {
            pontosBase = 10;
        } else if ("Capilar".equalsIgnoreCase(this.tipoDeUso) || "Corporal".equalsIgnoreCase(this.tipoDeUso)) {
            pontosBase = 8;
        } else if ("Unhas".equalsIgnoreCase(this.tipoDeUso)) {
            pontosBase = 6;
        } else {
            pontosBase = 4;
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