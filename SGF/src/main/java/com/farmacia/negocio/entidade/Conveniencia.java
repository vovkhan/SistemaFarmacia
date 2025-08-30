package com.farmacia.negocio.entidade;

public class Conveniencia extends Produto {
    private final boolean ehAlimento;

    public Conveniencia(String nome, double preco, String fabricante, int estoqueMinimo, boolean ehAlimento) {
        super(nome, preco, fabricante, estoqueMinimo);
        this.ehAlimento = ehAlimento;
    }

    @Override
    public int calcularPontosGerados(int quantidadeVendida) {
        int pontosBase = 2;

        if (this.ehAlimento) {
            pontosBase++;
        }

        return pontosBase * quantidadeVendida;
    }

    public boolean isAlimento() {
        return ehAlimento;
    }

    @Override
    public String toString() {
        return super.toString() + " | Categoria: Conveniência" + (ehAlimento ? " (Alimento)" : "");
    }
}