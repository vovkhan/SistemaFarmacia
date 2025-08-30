package com.farmacia.negocio.servico;

import com.farmacia.negocio.entidade.Produto;
import java.util.Objects;

public class ContadorProduto {
    private Produto produto;
    private int quantidadeTotal;

    public ContadorProduto(Produto produto, int quantidadeInicial) {
        this.produto = produto;
        this.quantidadeTotal = quantidadeInicial;
    }

    public void adicionarQuantidade(int quantidade) {
        this.quantidadeTotal += quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContadorProduto that = (ContadorProduto) o;
        return Objects.equals(produto, that.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produto);
    }
}