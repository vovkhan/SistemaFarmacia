package com.farmacia.negocio.excecao.venda;

public class UsoDePontosNaoPermitidoException extends VendaException {
  public UsoDePontosNaoPermitidoException() {
    super("Pontos de fidelidade só podem ser utilizados por clientes cadastrados.");
  }
}
