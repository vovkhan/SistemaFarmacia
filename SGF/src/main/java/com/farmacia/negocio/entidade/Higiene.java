package com.farmacia.negocio.entidade;

public class Higiene extends Produto {
    private final boolean ehInfantil;

    public Higiene(String nome, double preco, String fabricante, boolean ehInfantil) {
        super(nome, preco, fabricante);
        this.ehInfantil = ehInfantil;
    }

    @Override
    public int calcularPontosGerados(int quantidadeVendida) {
        int pontosBase = 8;

        if (this.ehInfantil) {
            pontosBase += 2;
        }

        return pontosBase * quantidadeVendida;
    }

    public boolean isEhInfantil() {
        return ehInfantil;
    }

    @Override
    public String toString() {
        return super.toString() + "\n | Categoria: Higiene" + (ehInfantil ? " (Infantil)" : "");

    }
}