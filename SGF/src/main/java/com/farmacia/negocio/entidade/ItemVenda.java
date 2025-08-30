package com.farmacia.negocio.entidade;

import com.farmacia.negocio.excecao.DadosInvalidosException;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa uma única linha em uma Venda.
 * Esta classe conecta um Produto a uma Venda, armazenando a quantidade vendida
 * e o preço unitário no momento da transação para garantir a integridade histórica dos dados.
 * É um objeto imutável por design: uma vez criado, não deve ser alterado.
 */
public class ItemVenda implements Serializable {
    private final Produto produto;
    private final int quantidade;
    private final double precoUnitarioGravado;

    public ItemVenda(Produto produto, int quantidade, double precoUnitarioGravado) {
        if (produto == null) {
            throw new DadosInvalidosException("O produto de um item de venda não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new DadosInvalidosException("A quantidade de um item de venda deve ser positiva.");
        }
        if (precoUnitarioGravado < 0) {
            throw new DadosInvalidosException("O preço de um item de venda não pode ser negativo.");
        }

        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitarioGravado = precoUnitarioGravado;
    }

    /**
     * Calcula o valor subtotal deste item.
     */
    public double calcularSubtotal() {
        return this.quantidade * this.precoUnitarioGravado;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitarioGravado() {
        return precoUnitarioGravado;
    }

    @Override
    public String toString() {
        return "Item: " + produto.getNome() +
                " | Qtd: " + quantidade +
                " | Preço Un.: R$" + String.format("%.2f", precoUnitarioGravado) +
                " | Subtotal: R$" + String.format("%.2f", calcularSubtotal());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemVenda itemVenda = (ItemVenda) o;
        return quantidade == itemVenda.quantidade &&
                Double.compare(itemVenda.precoUnitarioGravado, precoUnitarioGravado) == 0 &&
                Objects.equals(produto, itemVenda.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produto, quantidade, precoUnitarioGravado);
    }
}