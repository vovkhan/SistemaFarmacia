package com.farmacia.negocio.excecao.venda;

public class PontosInsuficientesException extends VendaException {
    public PontosInsuficientesException() {
        super("O cliente não possui pontos suficientes para o resgate.");
    }
}
