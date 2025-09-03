package com.farmacia.negocio.excecao.venda;

public class VendaNaoEncontradaException extends VendaException {
    public VendaNaoEncontradaException(String codigo) {
        super("Venda com código '" + codigo + "' não encontrada.");
    }
}
