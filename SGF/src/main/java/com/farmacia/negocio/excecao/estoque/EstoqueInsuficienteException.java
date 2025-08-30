package com.farmacia.negocio.excecao.estoque;

public class EstoqueInsuficienteException extends EstoqueException {
    public EstoqueInsuficienteException(String nomeProduto) {
        super("Estoque insuficiente para o produto: " + nomeProduto);
    }
}
