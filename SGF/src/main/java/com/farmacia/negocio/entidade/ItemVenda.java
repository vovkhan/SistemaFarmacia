package com.farmacia.negocio.entidade;

public class ItemVenda {
    private Produto produto;

    private int quantidade;

    private double precoUnitarioGravado;

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitarioGravado() {
        return precoUnitarioGravado;
    }

    public void setPrecoUnitarioGravado(double precoUnitarioGravado) {
        this.precoUnitarioGravado = precoUnitarioGravado;
    }

    public double calcularSubtotal(){
        return quantidade * precoUnitarioGravado;
    }
}
