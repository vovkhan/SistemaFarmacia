package com.farmacia.negocio.entidade;

public class ItemVenda {
    private final Produto produto;
    private final int quantidade;
    private final double precoUnitarioGravado;

    public ItemVenda(Produto produto, int quantidade, double precoUnitarioGravado) {
        if (produto == null) {
            //O item tem que ter um produto
        }
        if (quantidade <= 0) {
            //O item tem que ter uma quantidade valida
        }
        if (precoUnitarioGravado < 0) {
            //O preço do produto tem que ser valido
        }

        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitarioGravado = precoUnitarioGravado;
    }

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
}