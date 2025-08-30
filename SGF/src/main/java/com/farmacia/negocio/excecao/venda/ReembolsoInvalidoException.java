package com.farmacia.negocio.excecao.venda;

public class ReembolsoInvalidoException extends VendaException {
    public ReembolsoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
