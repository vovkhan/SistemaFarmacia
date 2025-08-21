package com.farmacia.negocio.entidade;

import java.util.Arrays;
import java.util.List;

public class Medicamento extends Produto {
    private static final List<String> TARJAS_VALIDAS = Arrays.asList("Sem Tarja", "Amarela", "Vermelha", "Preta");
    private final boolean necessitaReceita;
    private final String tarja;

    public Medicamento(String nome, double preco, String fabricante, int estoqueMinimo, boolean necessitaReceita, String tarja) {
        super(nome, preco, fabricante, estoqueMinimo);
        this.necessitaReceita = necessitaReceita;

        if (!isTarjaValida(tarja)) {
           //Tem que ser uma Tarja Valida
        }

        this.tarja = tarja;
    }

    private boolean isTarjaValida(String tarja) {
        if (tarja == null || tarja.trim().isEmpty()) {
            return false;
        }
        for (String valida : TARJAS_VALIDAS) {
            if (valida.equalsIgnoreCase(tarja.trim())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int calcularPontosGerados(int quantidadeVendida) {
        int pontosBase;

        if ("Preta".equalsIgnoreCase(this.tarja)) {
            pontosBase = 12;
        } else if ("Amarela".equalsIgnoreCase(this.tarja)) {
            pontosBase = 9;
        } else if ("Vermelha".equalsIgnoreCase(this.tarja)) {
            pontosBase = 6;
        } else if ("Sem Tarja".equalsIgnoreCase(this.tarja)) {
            pontosBase = 3;
        } else{
            pontosBase = 2;
        }
        return pontosBase * quantidadeVendida;
    }

    public boolean EhNecessitaReceita() {
        return necessitaReceita;
    }

    public String getTarja() {
        return tarja;
    }
}